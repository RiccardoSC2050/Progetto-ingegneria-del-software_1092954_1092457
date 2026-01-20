package it.unibg.progetto.api.app;

import java.awt.EventQueue;
import it.unibg.progetto.gui.controller.TerminalController;
import it.unibg.progetto.gui.view.TerminalFrame;

public class GuiMain {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			TerminalFrame view = new TerminalFrame();
			TerminalController controller = new TerminalController(view);
			controller.init();
			view.setVisible(true);
		});
	}
}
