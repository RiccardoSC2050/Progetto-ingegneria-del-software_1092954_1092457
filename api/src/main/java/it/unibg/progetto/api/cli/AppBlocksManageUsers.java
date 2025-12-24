package it.unibg.progetto.api.cli;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.servlet.mvc.method.annotation.SessionAttributeMethodArgumentResolver;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.api.application.dto.RootDto;
import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.application.usecase.UsersUseCase;
import it.unibg.progetto.api.cli.components.Exit;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.cli.components.Quit;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.Validators;
import it.unibg.progetto.api.domain.rules.InvalidValues;
import it.unibg.progetto.api.security.Hash;
import it.unibg.progetto.api.security.session.SessionManager;

public class AppBlocksManageUsers {

	public AppBlocksManageUsers() {
	}

	public void rootCreation(Root root) {
		try {

			System.out.println("NOME SISTEMA: " + InvalidValues.ROOT.toString());
			String pw = "";
			do {
				System.out.print("Inserire PASSWORD di SISTEMA: ");
				pw = GlobalScanner.scanner.nextLine().strip();
			} while (!Root.getInstanceRoot().checkLenghtPw(pw));
			root = new Root(Hash.hash(pw));
			UsersUseCase.getInstance().addRootOnData(root);
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
			RootDto rootdto = UsersUseCase.getInstance().rootIsOnData();
			if (rootdto == null && UsersUseCase.getInstance().numberOfAllOperators() == 0) {
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

	private Validators rootIsAlone() {
		RootDto rootdto = UsersUseCase.getInstance().rootIsOnData();
		if (rootdto == null)
			return Validators.neutral;
		if (UsersUseCase.getInstance().numberOfAllOperators() > 1) {
			return Validators.negative;
		}
		return Validators.affermative;

	}

	public void loginSession() {
		Validators flag;
		String name = "";
		String pw = "";
		do {

			if (rootIsAlone().equals(Validators.neutral)) {// root is diapered from data
				System.out.println("errore di sistema, ROOT inesistente [correzione immediata]");
				Root.createRootErrorDatabase();

			} else if (rootIsAlone().equals(Validators.affermative)) {
				Validators c = changeRootOrCreateUsere();
				do {
					if (c.equals(Validators.neutral))
						System.out.println("Spiacente è importante scegliere");

				} while (c.equals(Validators.neutral));

				if (c.equals(Validators.rootLog)) {
					return;
				}
			}
			System.out.print("[LOGIN] Inserire nome utente: ");
			name = GlobalScanner.scanner.nextLine().strip().toLowerCase();
			Exit.exit(name); // exit

			System.out.print("[LOGIN] Inserire password: ");
			pw = GlobalScanner.scanner.nextLine().strip().toLowerCase();
			Exit.exit(pw); // exit

			flag = Master.getIstance().login(name, pw);
		} while (flag.equals(Validators.negative));

		switch (flag) {
		case affermative:
			System.out.println("Connesso");
			SessionManager.accessLevelOption();
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
		Validators flag;
		do {
			System.out.println("[ROOT LOGIN] Nome: ROOT");
			System.out.print("[ROOT LOGIN] Inserire password: ");
			String pw = GlobalScanner.scanner.nextLine().trim();
			Exit.exit(pw); // exit
			if (Quit.quit(pw))
				return false;
			flag = Master.getIstance().login(InvalidValues.ROOT.toString().toLowerCase(), pw);
		} while (flag == Validators.negative || flag == Validators.neutral);
		System.out.println("Connesso come ROOT");
		return true;

	}

	public boolean createUserSession() {
		return Root.getInstanceRoot().createUser();
	}

	public Validators changeRootOrCreateUsere() {
		boolean f = true;
		do {
			System.out.println("Vuoi accedere come Root o vuoi creare un nuovo utente? [1|2]");
			System.out.println("- 1 per accere come ROOT");
			System.out.println("- 2 per creare nuovo utente");
			String answare = GlobalScanner.scanner.nextLine().strip();
			if (Quit.quit(answare))
				return Validators.neutral;
			switch (answare) {
			case "1":
				boolean n = RootLoginSession();
				if (n)
					return Validators.rootLog;

				break;

			case "2":
				boolean n1 = createUserSession();
				if (n1)
					return Validators.creationUser;

				break;

			default:
				break;
			}
		} while (f);
		return Validators.negative;
	}

	public void logoutSession() {

		Master.getIstance().logout();

		loginSession();
	}

	public void createUserIfRoot() {
		if (SessionManager.getCurrent().getAccessLevel() == AccessLevel.AL5.getLevel()) {
			Root.getInstanceRoot().createUser();
		} else {
			System.out.println("Non sei ROOT, non puoi creare utente");
		}
	}

	public void deleteUserIfRoot() {
		if (SessionManager.getCurrent().getAccessLevel() == AccessLevel.AL5.getLevel()) {
			Root.getInstanceRoot().deleteUser();
			;
		} else {
			System.out.println("Non sei ROOT, non puoi cancellare utenti");
		}
	}

	public void showUsersIfRoot(Validators n) {
		if (SessionManager.getCurrent().getAccessLevel() == AccessLevel.AL5.getLevel()) {
			System.out.println("numero utenti: " + UsersUseCase.getInstance().numberOfAllOperators() + "\n");
			UsersUseCase.getInstance().printNameUserAll(n,
					UsersUseCase.getInstance().trasformListUsersIntoListUserWithoutPassword());

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

		String myId = SessionManager.getCurrent().getUuid();

		List<User> allUsers = UsersUseCase.getInstance().trasformListUsersIntoListUserWithoutPassword();

		List<CsvDto> allCsv = CsvUseCase.getIstnce().returnAllFileCsvDtoFromData();

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

		if (SessionManager.getCurrent().getAccessLevel() < AccessLevel.AL3.getLevel()) {
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
			List<CsvDto> userFiles = CsvUseCase.getIstnce().returnAllFileCsvDtoFromDataOfUser(selectedUser.getId());

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
			CsvUseCase.getIstnce().showFileContent(chosenFileName, selectedUser.getId());
		} while (true);
	}

	private Integer readChoice(int min, int max, String prompt) {
		while (true) {
			System.out.print(prompt);
			String s = GlobalScanner.scanner.nextLine().strip();
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

	public void changePassword() {
		String id = SessionManager.getCurrent().getUuid();
		String pw = "";
		do {
			System.out.print("inseire la nuova password: ");
			pw = GlobalScanner.scanner.nextLine().strip();
		} while (!Root.getInstanceRoot().checkLenghtPw(pw));
		UsersUseCase.getInstance().changePassordToUser(Hash.hash(pw), id);
	}

	public void changeAccLev() {
		if (SessionManager.getCurrent().getAccessLevel() != AccessLevel.AL5.getLevel()) {
			System.out.println("Non hai i permessi root per modificare");
		}
		boolean f;
		String id = "";
		do {
			f = true;
			System.out.println("Scegli a quale utente modificare il livello di acccesso");
			List<User> ulist = UsersUseCase.getInstance().trasformListUsersIntoListUserWithoutPassword();
			UsersUseCase.getInstance().printNameUserAll(Validators.neutral, ulist);
			String input = GlobalScanner.scanner.nextLine();
			for (User u : ulist) {
				if (u.getName().equals(input)) {
					f = false;
					id = u.getId();
					break;
				}
			}

		} while (f);

		int i;
		do {
			System.out.print("inseire il nuovo livello di accesso: ");
			String in = GlobalScanner.scanner.nextLine().strip();
			i = Integer.parseInt(in);
		} while (!AccessLevel.isAPossibleValue(i));
		UsersUseCase.getInstance().changeAccessLevelToUser(i, id);
	}

}
