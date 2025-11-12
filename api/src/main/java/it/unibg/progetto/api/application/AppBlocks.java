package it.unibg.progetto.api.application;

import it.unibg.progetto.api.components.GlobalScaner;

public class AppBlocks {

	public AppBlocks() {
	}

	public void loginSession() {
		System.out.println("inserire nome utente: ");
		String name = GlobalScaner.scanner.nextLine();
		System.out.println("inserire password: ");
		String pw = GlobalScaner.scanner.nextLine();

		boolean flag = Master.getIstance().login(name, pw);
		if (flag) {
			System.out.println("connesso");
			return;
		} else {
			loginSession();
		}
	}
}
