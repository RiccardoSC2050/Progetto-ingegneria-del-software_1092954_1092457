package it.unibg.progetto.gui.bridge;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public ConsoleBridge(TerminalFrame view) {
        this.view = view;
    }

    public void start() throws IOException {
        if (!started.compareAndSet(false, true)) return;

        // 1) Pipe: GUI -> "System.in" (Scanner)
        inPipe = new PipedInputStream();
        outPipe = new PipedOutputStream(inPipe);
        writer = new PrintWriter(new OutputStreamWriter(outPipe, StandardCharsets.UTF_8), true);

        GlobalScanner.setScanner(new Scanner(inPipe, StandardCharsets.UTF_8));

        // 2) Redirect System.out/err -> JTextArea
        originalOut = System.out;
        originalErr = System.err;

        System.setOut(new PrintStream(new TextAreaOutputStream(view), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new TextAreaOutputStream(view), true, StandardCharsets.UTF_8));
    }

    public void sendLine(String line) {
        if (!started.get()) return;
        writer.println(line);   // IMPORTANT: newline = nextLine() in Scanner sblocca
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

    // OutputStream che scrive nella JTextArea in modo thread-safe
    static class TextAreaOutputStream extends OutputStream {
        private final TerminalFrame view;
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        TextAreaOutputStream(TerminalFrame view) {
            this.view = view;
        }

        @Override
        public void write(int b) {
            if (b == '\n') flushBuffer();
            else buffer.write(b);
        }

        @Override
        public void flush() {
            flushBuffer();
        }

        private void flushBuffer() {
            String text = buffer.toString(StandardCharsets.UTF_8);
            buffer.reset();
            if (text.isEmpty()) return;

            SwingUtilities.invokeLater(() -> view.appendLine(text));
        }
    }
}
