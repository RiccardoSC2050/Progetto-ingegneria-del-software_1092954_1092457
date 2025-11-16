package it.unibg.progetto.api.application;

import it.unibg.progetto.api.components.Exit;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.components.Quit;
import it.unibg.progetto.api.conditions.Checks;

public class AppBlocks {

	public AppBlocks() {
	}

	public void RootConfiguration() {

	}

	/**
	 * 
	 */
	public void loginSession() {
		Checks flag;
		do {
			System.out.print("[LOGIN] Inserire nome utente: ");
			String name = GlobalScaner.scanner.nextLine();
			Exit.exit(name); // exit
			System.out.print("[LOGIN] Inserire password: ");
			String pw = GlobalScaner.scanner.nextLine();
			Exit.exit(pw); // exit

			flag = Master.getIstance().login(name, pw);
		} while (flag == Checks.negative);

		switch (flag) {
		case affermative:
			System.out.println("connesso");
			break;

		case neutral:
			System.out.println("Come vuoi procedere?");
			changeRootOrCreateUsere();
			break;

		default:
			break;
		}

	}

	private void changeRootOrCreateUsere() {
		boolean f = true;
		while (f) {
			System.out.println("Vuoi accedere come Root o vuoi creare un nuovo utente? [1|2]");
			System.out.println("- 1 per accere a ROOT");
			System.out.println("- 2 per creare nuovo utente");
			String answare = GlobalScaner.scanner.nextLine();
			switch (answare) {
			case "1":

				f = false;
				break;

			case "2":

				f = false;
				break;

			default:
			}
		}

	}

	public void logoutSession(String logout) {
		if (logout.equals("u-logout")) {
			Master.getIstance().logout();
		}
		loginSession();
	}
}
