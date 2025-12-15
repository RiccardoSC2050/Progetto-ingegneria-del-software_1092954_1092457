package it.unibg.progetto.api.cli;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.api.application.dto.Rootdto;
import it.unibg.progetto.api.application.usecase.ActionOnCsv;
import it.unibg.progetto.api.application.usecase.ActionOnUseRS;
import it.unibg.progetto.api.cli.components.Exit;
import it.unibg.progetto.api.cli.components.GlobalScaner;
import it.unibg.progetto.api.cli.components.Quit;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.Checks;
import it.unibg.progetto.api.domain.rules.StrangeValues;
import it.unibg.progetto.api.security.Hash;
import it.unibg.progetto.api.security.ManagerSession;

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
		} while (flag == Checks.negative || flag == Checks.neutral);
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

	/**
	 * NOMI UTENTI CHE HANNO ALMENO UN FILE
	 */
	/**
	 * Ritorna SOLO gli altri utenti (escludo me stesso) che hanno almeno un file
	 * CSV.
	 */
	private List<User> getOtherUsersThatHaveAtLeastOneFile() {

		String myId = ManagerSession.getCurrent().getUuid();

		List<User> allUsers = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

		List<CsvDto> allCsv = ActionOnCsv.getIstnce().returnAllFileCsvDtoFromData();

		List<String> userIdsWithFiles = new ArrayList<>();

		// utenti che hanno almeno un file
		for (CsvDto c : allCsv) {
			if (c == null)
				continue;
			String uid = c.getOwnerId();
			if (uid != null && !uid.isBlank() && !userIdsWithFiles.contains(uid)) {
				userIdsWithFiles.add(uid);
			}
		}

		List<User> result = new ArrayList<>();

		for (User u : allUsers) {
			if (u == null || u.getId() == null)
				continue;

			boolean isMe = u.getId().equals(myId);
			boolean hasFile = userIdsWithFiles.contains(u.getId());
			boolean isRoot = u.getId().equals("0");

			if (!isMe && hasFile && !isRoot) {
				result.add(u);
			}
		}

		return result;
	}

	/**
	 * processo logico per visualizzare il file di un utente
	 * 
	 * @throws Exception
	 */
	public void viewOtherFiles() throws Exception {

		if (ManagerSession.getCurrent().getAccessLevel() < AccessLevel.AL3.getLevel()) {
			System.out.println("Non sei di livello 3, non puoi accedere alla lista file di altri utenti");
			return;
		}
		do {
			List<User> otherUsersWithFiles = getOtherUsersThatHaveAtLeastOneFile();

			System.out.println("<=== UTENTI CHE HANNO FILE CSV ===>");
			if (otherUsersWithFiles.isEmpty()) {
				System.out.println("Non ci sono altri utenti con file CSV disponibili.");
				return;
			}

			// stampa elenco utenti con indice
			int i = 0;
			for (User u : otherUsersWithFiles) {
				System.out.println((i + 1) + ") " + u.getName());
				i++;
			}

			// scelta utente

			Integer userChoice = readChoice(1, otherUsersWithFiles.size(), "Seleziona un utente (numero): ");
			if (userChoice == null)
				return;
			if (userChoice == -1)
				return;
			User selectedUser = otherUsersWithFiles.get(userChoice - 1);

			System.out.println("\nI suoi file:");
			List<CsvDto> userFiles = ActionOnCsv.getIstnce().returnAllFileCsvDtoFromDataOfUser(selectedUser.getId());

			if (userFiles == null || userFiles.isEmpty()) {
				// teoricamente non dovrebbe succedere (li abbiamo filtrati), ma meglio essere
				// solidi
				System.out.println("Questo utente non ha file CSV.");
				return;
			}

			// lista fileName unica e stabile
			List<String> fileNames = new ArrayList<>();
			Set<String> seen = new HashSet<>();
			for (CsvDto c : userFiles) {
				if (c == null)
					continue;
				String fn = c.getFileName();
				if (fn != null && !fn.isBlank() && seen.add(fn)) {
					fileNames.add(fn);
				}
			}

			if (fileNames.isEmpty()) {
				System.out.println("Questo utente non ha file CSV.");
				return;
			}

			// stampa file con indice
			i = 0;
			for (String n : fileNames) {
				System.out.println((i + 1) + ") " + fileNames.get(i));
				i++;
			}

			// scelta file

			Integer fileChoice = readChoice(1, fileNames.size(), "Seleziona un file da leggere (numero): ");
			if (fileChoice == null)
				return;

			String chosenFileName = fileNames.get(fileChoice - 1);

			// mostra contenuto file
			ActionOnCsv.getIstnce().showFileContent(chosenFileName, selectedUser.getId());
		} while (true);
	}

	private Integer readChoice(int min, int max, String prompt) {
		while (true) {
			System.out.print(prompt);
			String s = GlobalScaner.scanner.nextLine().strip();
			if (Quit.quit(s))
				return null;
			try {
				int n = Integer.parseInt(s);
				if (n >= min && n <= max)
					return n;
			} catch (NumberFormatException ignored) {
			}
			System.out.println("Scelta non valida. Inserisci un numero tra " + min + " e " + max + ".");
		}
	}

}
