package it.unibg.progetto.api.application;

import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.components.Exit;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.components.Quit;
import it.unibg.progetto.api.conditions.Checks;
import it.unibg.progetto.api.conditions.StrangeValues;
import it.unibg.progetto.api.dto.Rootdto;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.hashcode.Hash;

public class AppBlocks {

	public AppBlocks() {
	}

	public void RootConfiguration(Root root) {
		try {
			Rootdto rootdto = ActionOnUseRS.getInstance().rootIsOnData();
			if (rootdto == null) {
				System.out.println("BENVENUTO, SI CONFIGURI IL PROGRAMMA");
				System.out.println("NOME SISTEMA: " + StrangeValues.ROOT.toString());
				System.out.print("Inserire PASSWORD di SISTEMA: ");
				String pw = Hash.hash(GlobalScaner.scanner.nextLine());
				root = new Root(pw);
				ActionOnUseRS.getInstance().addRootOnData(root);
				System.out.println();
				System.out.println("PASSWORD SALVATA CON SUCCESSO");

				changeRootOrCreateUsere();
			} else {
				root = new Root(rootdto.getPassword());
			}
		} catch (Exception e) {
			System.err.println("Error creating Root instance: " + e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void loginSession() {
		Checks flag;
		do {
			System.out.print("[LOGIN] Inserire nome utente: ");
			String name = GlobalScaner.scanner.nextLine().toLowerCase();
			Exit.exit(name); // exit
			if (Quit.quit(name))
				return;

			System.out.print("[LOGIN] Inserire password: ");
			String pw = GlobalScaner.scanner.nextLine().toLowerCase();
			Exit.exit(pw); // exit
			if (Quit.quit(name))
				return;

			flag = Master.getIstance().login(name, pw);
		} while (flag == Checks.negative);

		switch (flag) {
		case affermative:
			System.out.println("Connesso");
			break;

		case neutral:
			System.out.println("Come vuoi procedere?");
			changeRootOrCreateUsere();
			break;

		default:
			break;
		}

	}

	public boolean RootLoginSession() {
		Checks flag;
		do {
			System.out.println("[ROOT LOGIN] Nome: ROOT");
			System.out.print("[ROOT LOGIN] Inserire password: ");
			String pw = GlobalScaner.scanner.nextLine();
			Exit.exit(pw); // exit
			if (Quit.quit(pw))
				return false;
			flag = Master.getIstance().login(StrangeValues.ROOT.toString().toLowerCase(), pw);
		} while (flag == Checks.negative && flag == Checks.neutral);
		System.out.println("Connesso come ROOT");
		return true;

	}

	public boolean createUserSession() {
		return Root.getInstanceRoot().createUser();
	}

	public void changeRootOrCreateUsere() {
		boolean f = true;
		while (f) {
			System.out.println("Vuoi accedere come Root o vuoi creare un nuovo utente? [1|2]");
			System.out.println("- 1 per accere come ROOT");
			System.out.println("- 2 per creare nuovo utente");
			String answare = GlobalScaner.scanner.nextLine();
			if (Quit.quit(answare))
				return;
			switch (answare) {
			case "1":
				boolean n = RootLoginSession();
				if (!n)
					break; // quit
				f = false;
				break;

			case "2":
				boolean n1 = createUserSession();
				if (!n1)
					break; // quit
				f = false;
				break;

			default:
			}
		}

	}

	public void logoutSession(String logout) {
		if (logout.equals("out")) {
			Master.getIstance().logout();
		}
		loginSession();
	}
}
