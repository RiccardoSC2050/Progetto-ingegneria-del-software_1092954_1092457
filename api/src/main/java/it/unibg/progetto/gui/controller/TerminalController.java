package it.unibg.progetto.gui.controller;

import it.unibg.progetto.api.app.ApiMain;
import it.unibg.progetto.gui.bridge.ConsoleBridge;
import it.unibg.progetto.gui.view.TerminalFrame;

public class TerminalController {

	private final TerminalFrame view;
	private final ConsoleBridge bridge;

	
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

		
		bridge.setOnOutput(this::captureBackendOutput);

		
		view.onSubmit(this::handleSubmit);
		view.onClear(this::handleClear);

		
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
		cmd = cmd.strip(); 

		view.clearInput();
		view.focusInput();

		if (cmd.isEmpty())
			return;

		
		view.appendLine("> " + cmd);

		
		bridge.sendLine(cmd);
	}

	private void handleClear() {
		view.clearOutput();

	
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

	
	private void captureBackendOutput(String text) {
		if (text == null)
			return;

		
		String s = text.replace("\r", "").strip();
		if (s.isEmpty())
			return;

		
		lastBackendMessage = s;
	}
}
