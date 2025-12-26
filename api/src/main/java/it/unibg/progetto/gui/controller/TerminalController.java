package it.unibg.progetto.gui.controller;

import it.unibg.progetto.api.app.ApiMain;
import it.unibg.progetto.gui.bridge.ConsoleBridge;
import it.unibg.progetto.gui.view.TerminalFrame;

public class TerminalController {

    private final TerminalFrame view;
    private final ConsoleBridge bridge;

    // Stato UI: ultima riga stampata nel terminale
    private String lastLine = "";

    public TerminalController(TerminalFrame view) {
        this.view = view;
        this.bridge = new ConsoleBridge(view);
    }

    public void init() {
        try {
            bridge.start();
        } catch (Exception e) {
            appendAndRemember("Errore bridge: " + e.getMessage());
            return;
        }

        // Se il tuo ConsoleBridge supporta l'hook, abilitalo:
        // bridge.setOnOutput(this::rememberLastLine);

        // Eventi GUI
        view.onSubmit(this::handleSubmit);
        view.onClear(this::handleClear);

        // Avvio backend/CLI in thread separato (non blocca la GUI)
        new Thread(() -> {
            try {
                ApiMain.main(new String[] { "--spring.main.web-application-type=none" });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, "backend-thread").start();

        appendAndRemember("GUI collegata. Scrivi comandi come nel terminale.");
        view.focusInput();
    }

    private void handleSubmit() {
        String cmd = view.getInputText();
        if (cmd == null) cmd = "";
        cmd = cmd.strip(); // se Java 8: trim()

        view.clearInput();
        view.focusInput();

        if (cmd.isEmpty()) return;

        // Echo comando (così resta nel log)
        appendAndRemember("> " + cmd);

        // Inoltra al programma esistente (che legge da GlobalScanner)
        bridge.sendLine(cmd);
    }

    private void handleClear() {
        view.clearOutput();

        // Mostra l’ultima stampa (se esiste)
        if (lastLine != null && !lastLine.isBlank()) {
            view.appendLine(lastLine);
        }

        view.focusInput();
    }

    public void shutdown() {
        bridge.stop();
    }

    // ===== Helpers =====

    private void appendAndRemember(String line) {
        view.appendLine(line);
        rememberLastLine(line);
    }

    private void rememberLastLine(String line) {
        if (line == null) return;
        String normalized = line.strip();
        if (!normalized.isEmpty()) {
            lastLine = normalized;
        }
    }
}
