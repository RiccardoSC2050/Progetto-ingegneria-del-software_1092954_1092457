package it.unibg.progetto.api.application;

import it.unibg.progetto.api.components.Exit;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.components.Quit;

public class AppBlocks {

	public AppBlocks() {
	}

	/**
	 * 
	 */
	public void loginSession() {
		boolean flag = false;
		do {
			System.out.println("inserire nome utente: ");
			String name = GlobalScaner.scanner.nextLine();
			Exit.exit(name); // exit
			System.out.println("inserire password: ");
			String pw = GlobalScaner.scanner.nextLine();
			Exit.exit(pw); // exit

			flag = Master.getIstance().login(name, pw);
		} while (!flag);

		System.out.println("connesso");

	}

	public void logoutSession(String logout) {
		if (logout.equals("u-logout")) {
			Master.getIstance().logout();
		}
		loginSession();
	}
}
