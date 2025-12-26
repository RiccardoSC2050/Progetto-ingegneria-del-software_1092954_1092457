package it.unibg.progetto.gui.controller;

import it.unibg.progetto.api.app.ApiMain;
import it.unibg.progetto.gui.bridge.ConsoleBridge;
import it.unibg.progetto.gui.view.TerminalFrame;

public class TerminalController {

	private final TerminalFrame view;
	private final ConsoleBridge bridge;

	// ✅ Ultima "richiesta" / ultimo output del BACKEND (quello che ti serve
	// rivedere)
	private String lastBackendMessage = "";

	public TerminalController(TerminalFrame view) {
		this.view = view;
		this.bridge = new ConsoleBridge(view);
	}

	public void init() {
		try {
			bridge.start();
		} catch (Exception e) {
			view.appendLine("Errore bridge: " + e.getMessage());
			return;
		}

		// ✅ intercetta l'output del backend e salva l'ultimo messaggio utile
		bridge.setOnOutput(this::captureBackendOutput);

		// Eventi GUI
		view.onSubmit(this::handleSubmit);
		view.onClear(this::handleClear);

		// Avvio backend/CLI
		new Thread(() -> {
			try {
				ApiMain.main(new String[] { "--spring.main.web-application-type=none" });
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}, "backend-thread").start();

		view.focusInput();
	}

	private void handleSubmit() {
		String cmd = view.getInputText();
		if (cmd == null)
			cmd = "";
		cmd = cmd.strip(); // Java 11+. Se Java 8: trim()

		view.clearInput();
		view.focusInput();

		if (cmd.isEmpty())
			return;

		// (opzionale) mostra il comando digitato
		view.appendLine("> " + cmd);

		// invia al programma esistente
		bridge.sendLine(cmd);
	}

	private void handleClear() {
		view.clearOutput();

		// ✅ dopo clear, ristampa l’ultima richiesta del BACKEND
		if (lastBackendMessage != null && !lastBackendMessage.isBlank()) {
			view.appendLine(lastBackendMessage);
		} else {
			view.appendLine("(nessun messaggio da ripristinare)");
		}

		view.focusInput();
	}

	public void shutdown() {
		bridge.stop();
	}

	// ====== qui decidiamo cosa considerare "ultima richiesta utile" ======
	private void captureBackendOutput(String text) {
		if (text == null)
			return;

		// normalizza
		String s = text.replace("\r", "").strip();
		if (s.isEmpty())
			return;

		// Qui: SALVIAMO l’ultimo output non vuoto del backend.
		// Questo copre "OK", "Inserisci ..." ecc.
		lastBackendMessage = s;
	}
}
