package it.unibg.progetto.gui.bridge;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.gui.view.TerminalFrame;

public class ConsoleBridge {

    private final TerminalFrame view;

    private PipedInputStream inPipe;
    private PipedOutputStream outPipe;
    private PrintWriter writer;

    private PrintStream originalOut;
    private PrintStream originalErr;

    private final AtomicBoolean started = new AtomicBoolean(false);

    // Hook: il controller può intercettare l’output del backend
    private volatile Consumer<String> onOutput;

    public ConsoleBridge(TerminalFrame view) {
        this.view = view;
    }

    public void setOnOutput(Consumer<String> onOutput) {
        this.onOutput = onOutput;
    }

    public void start() throws IOException {
        if (!started.compareAndSet(false, true)) return;

        // 1) Pipe: GUI -> Scanner (GlobalScanner)
        inPipe = new PipedInputStream();
        outPipe = new PipedOutputStream(inPipe);
        writer = new PrintWriter(new OutputStreamWriter(outPipe, StandardCharsets.UTF_8), true);

        GlobalScanner.setScanner(new Scanner(inPipe, StandardCharsets.UTF_8));

        // 2) Redirect System.out/err -> JTextArea
        originalOut = System.out;
        originalErr = System.err;

        TextAreaOutputStream taos = new TextAreaOutputStream(view, s -> {
            Consumer<String> c = this.onOutput;
            if (c != null) c.accept(s);
        });

        System.setOut(new PrintStream(taos, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(taos, true, StandardCharsets.UTF_8));
    }

    public void sendLine(String line) {
        if (!started.get()) return;
        writer.println(line); // newline => Scanner.nextLine() sblocca
        writer.flush();
    }

    public void stop() {
        if (!started.compareAndSet(true, false)) return;

        try { if (writer != null) writer.close(); } catch (Exception ignored) {}
        try { if (outPipe != null) outPipe.close(); } catch (Exception ignored) {}
        try { if (inPipe != null) inPipe.close(); } catch (Exception ignored) {}

        if (originalOut != null) System.setOut(originalOut);
        if (originalErr != null) System.setErr(originalErr);
    }

    // OutputStream -> JTextArea, thread-safe + callback per controller
    static class TextAreaOutputStream extends OutputStream {
        private final TerminalFrame view;
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private final Consumer<String> callback;

        TextAreaOutputStream(TerminalFrame view, Consumer<String> callback) {
            this.view = view;
            this.callback = callback;
        }

        @Override
        public synchronized void write(int b) {
            // Accumula SEMPRE: print() può arrivare a pezzetti
            buffer.write(b);

            // quando arriva newline, flushiamo il buffer (linea completa)
            if (b == '\n') {
                flushBuffer();
            }
        }

        @Override
        public synchronized void flush() {
            // importante per print() senza newline
            flushBuffer();
        }

        private void flushBuffer() {
            String text = buffer.toString(StandardCharsets.UTF_8);
            buffer.reset();

            if (text == null || text.isEmpty()) return;

            // normalizza Windows CRLF
            text = text.replace("\r", "");

            // manda al controller (per salvare ultima domanda/OK ecc.)
            if (callback != null) callback.accept(text);

            // ✅ stampa RAW in GUI (non aggiungere newline extra)
            final String toShow = text;
            SwingUtilities.invokeLater(() -> view.appendRaw(toShow));
        }
    }
}
