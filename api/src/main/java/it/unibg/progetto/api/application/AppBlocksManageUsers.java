package it.unibg.progetto.api.application;

import java.util.List;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.components.Exit;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.components.Quit;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.Checks;
import it.unibg.progetto.api.conditions.StrangeValues;
import it.unibg.progetto.api.dto.Rootdto;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.api.operators.User;
import it.unibg.progetto.hashcode.Hash;

public class AppBlocksManageUsers {

	public AppBlocksManageUsers() {
	}

	public void rootCreation(Root root) {
		try {

			System.out.println("NOME SISTEMA: " + StrangeValues.ROOT.toString());
			System.out.print("Inserire PASSWORD di SISTEMA: ");
			String pw = Hash.hash(GlobalScaner.scanner.nextLine());
			root = new Root(pw);
			ActionOnUseRS.getInstance().addRootOnData(root);
			System.out.println();
			System.out.println("PASSWORD SALVATA CON SUCCESSO");

		} catch (Exception e) {
			System.err.println("Error creating Root instance: " + e.getMessage());
		}
	}

	public void RootConfiguration(Root root) {
		try {
			boolean f = true;
			AppBlocksManageCsv abCsv = new AppBlocksManageCsv();
			Rootdto rootdto = ActionOnUseRS.getInstance().rootIsOnData();
			if (rootdto == null && ActionOnUseRS.getInstance().numberOfAllOperators() == 0) {
				System.out.print("PRIMO ACCESSO, CREAZIONE NUOVO SISTEMA");
				rootCreation(root);
				abCsv.manageImplementationOfMainFileCsv();
				do {
					if (createUserSession()) {
						f = false;
						return;
					} else {
						System.out.println("Spiecente: è obbligatorio creare un utente nel proceso di configurazione");
					}
				} while (f);
			} else {
				Root.getInstanceRoot();
			}
		} catch (Exception e) {
			System.err.println("Error creating Root instance: " + e.getMessage());
		}
	}

	private Checks rootIsAlone() {
		Rootdto rootdto = ActionOnUseRS.getInstance().rootIsOnData();
		if (rootdto == null)
			return Checks.neutral;
		if (ActionOnUseRS.getInstance().numberOfAllOperators() > 1) {
			return Checks.negative;
		}
		return Checks.affermative;

	}

	public void loginSession() {
		Checks flag;
		do {

			if (rootIsAlone().equals(Checks.neutral)) {// root is diapered from data
				System.out.println("errore di sistema, ROOT inesistente [correzione immediata]");
				Root.createRootErrorDatabase();

			} else if (rootIsAlone().equals(Checks.affermative)) {
				Checks c = changeRootOrCreateUsere();
				do {
					if (c.equals(Checks.neutral))
						System.out.println("Spiacente è importante scegliere");

				} while (c.equals(Checks.neutral));

				if (c.equals(Checks.rootLog)) {
					return;
				}
			}
			System.out.print("[LOGIN] Inserire nome utente: ");
			String name = GlobalScaner.scanner.nextLine().toLowerCase();
			Exit.exit(name); // exit

			System.out.print("[LOGIN] Inserire password: ");
			String pw = GlobalScaner.scanner.nextLine().toLowerCase();
			Exit.exit(pw); // exit

			flag = Master.getIstance().login(name, pw);
		} while (flag.equals(Checks.negative));

		switch (flag) {
		case affermative:
			System.out.println("Connesso");
			break;

		case neutral:
			System.out.println("Errore, nessun utente nel database, riconfigurare macchina");
			Root.configurationOfRoot();
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

	public Checks changeRootOrCreateUsere() {
		boolean f = true;
		do {
			System.out.println("Vuoi accedere come Root o vuoi creare un nuovo utente? [1|2]");
			System.out.println("- 1 per accere come ROOT");
			System.out.println("- 2 per creare nuovo utente");
			String answare = GlobalScaner.scanner.nextLine();
			if (Quit.quit(answare))
				return Checks.neutral;
			switch (answare) {
			case "1":
				boolean n = RootLoginSession();
				if (n)
					return Checks.rootLog;

				break;

			case "2":
				boolean n1 = createUserSession();
				if (n1)
					return Checks.creationUser;

				break;

			default:
				break;
			}
		} while (f);
		return Checks.negative;
	}

	public void logoutSession() {

		Master.getIstance().logout();

		loginSession();
	}

	public void createUserIfRoot() {
		if (ManagerSession.getCurrent().getAccessLevel() == AccessLevel.AL5.getLevel()) {
			Root.getInstanceRoot().createUser();
		} else {
			System.out.println("Non sei ROOT, non puoi creare utente");
		}
	}

	public void deleteUserIfRoot() {
		if (ManagerSession.getCurrent().getAccessLevel() == AccessLevel.AL5.getLevel()) {
			Root.getInstanceRoot().deleteUser();
		} else {
			System.out.println("Non sei ROOT, non puoi cancellare utenti");
		}
	}

	public void showUsersIfRoot(Checks n) {
		if (ManagerSession.getCurrent().getAccessLevel() == AccessLevel.AL5.getLevel()) {
			System.out.println("numero utenti: " + ActionOnUseRS.getInstance().numberOfAllOperators() + "\n");
			ActionOnUseRS.getInstance().printNameUserAll(n,
					ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword());

			System.out.println();

		} else {
			System.out.println("Non sei ROOT, non puoi accedere alla lista utenti");
		}
	}

}
