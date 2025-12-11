package it.unibg.progetto.api.operators;

import java.util.Iterator;
import java.util.List;

import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.application.AppBlocksManageUsers;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.components.Input;
import it.unibg.progetto.api.components.Quit;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.Checks;
import it.unibg.progetto.api.conditions.StrangeValues;
import it.unibg.progetto.api.components.Exit;
import it.unibg.progetto.api.dto.Rootdto;

import it.unibg.progetto.hashcode.Hash;

/**
 * Root administrator class extending Operator with maximum privileges.
 * Implements singleton pattern to ensure only one root user exists. Has fixed
 * ID "0" and access level 5 for administrative operations.
 * 
 * @author Employee Management System
 * @version 1.0
 */
public class Root extends Operator implements DataControl {

	private static Root root = null;

	/**
	 * Constructs a Root operator with administrator privileges. Sets fixed ID "0"
	 * and access level 5 through parent constructor.
	 * 
	 * @param name     the username for the root administrator
	 * @param password the password for root authentication
	 */
	public Root(String password) {
		super(password);
	}

	private static Root resetRoot() {
		return root = null;
	}

	public static Root createRootErrorDatabase() {
		resetRoot();
		AppBlocksManageUsers ab = new AppBlocksManageUsers();
		ab.rootCreation(root);
		return getInstanceRoot();
	}

	/**
	 * this method use the static root to create a unique instance of root; used in
	 * main
	 * 
	 * @return Root instance
	 */
	public static Root getInstanceRoot() {
		if (root == null) {
			ActionOnUseRS service = ActionOnUseRS.getInstance();

			// Caso estremo: service non ancora inizializzato (fuori da Spring)
			if (service == null) {
				// password “di servizio” qualsiasi, basta che non sia vuota
				root = new Root(StrangeValues.secret.toString());
				return root;
			}

			Rootdto rootdto = service.rootIsOnData();

			if (rootdto != null && rootdto.getPassword() != null && !rootdto.getPassword().isBlank()) {
				// Root già presente sul DB → uso la password del DB
				root = new Root(rootdto.getPassword());
			} else {
				// Root NON presente sul DB (impossibile)

			}
		}
		return root;
	}

	public static void configurationOfRoot() {
		AppBlocksManageUsers ab = new AppBlocksManageUsers();
		ab.RootConfiguration(root);
	}

	/**
	 * create a User user = new User user is added to allOperators List
	 * 
	 * @param name
	 * @param pw
	 * @param al
	 * @return
	 * @throws InvalidAccessLevelException
	 */
	private Checks isPossibleTocreateUser(String name) {
		try {

			List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();
			if (userList != null) {
				for (User u : userList) {
					if (u.getName().equals(name)) {

						System.err.println("ERRORE di inserimento: il nome " + name
								+ " utente già esiste, si prega di inserirne uno nuovo");
						return Checks.negative;
					}
				}
			}
			System.out.println("Inserire password utente: ");
			String pw = GlobalScaner.scanner.nextLine();
			if (Quit.quit(pw))
				return Checks.neutral;
			pw = Hash.hash(pw);
			int aclv;
			do {

				System.out.println("Inserire il livello di accesso utente [1-3]: ");
				String al = GlobalScaner.scanner.nextLine();
				if (Quit.quit(pw))
					return Checks.neutral;
				if (Input.isNumeric(al) && !al.isEmpty())
					aclv = Integer.parseInt(al);
				else
					aclv = 0;

			} while (!(aclv > 0 && aclv <= 3));

			AccessLevel alv = AccessLevel.fromLevel(aclv);

			User user = new User(name, pw, alv);
			ActionOnUseRS.getInstance().addUserOnData(user);

			System.out.println("Utente creato con successo:");
			System.out.println(user.toString());

		} catch (ExceptionInInitializerError e) {
			System.out.println("ERRORE: non è stato possibile creare l'utente");
		}
		return Checks.affermative;
	}

	public boolean createUser() {
		Checks action = Checks.negative;
		do {
			System.out.println("CREAZIONE NUOVO UTENTE\n");
			System.out.println("Inserire nome utente: ");
			String name = GlobalScaner.scanner.nextLine().toLowerCase();
			if (Quit.quit(name))
				return false;

			action = isPossibleTocreateUser(name);
			if (action == Checks.neutral)
				return false;

		} while (action == Checks.negative);
		return true;

	}

	/**
	 * the main method to delete user
	 * 
	 * @throws InvalidAccessLevelException
	 */
	public void deleteUser() {

		List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

		if (userList == null) {
			System.out.println("Nessun utente da eliminare");
			return;
		}

		String n = userNameControl();
		if (n == "")
			return;
		String id = userIdControl(n);
		delUser(n, id);

	}

	@Override
	public void readDataFile() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createDataFile() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteDataFile() {
		// TODO Auto-generated method stub

	}

	/**
	 * it's an under method used by deleteUser()
	 * 
	 * @param name
	 * @param id
	 */
	private void delUser(String name, String id) {
		try {

			name = name.toLowerCase();
			List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

			for (User u : userList) {
				if (u.getName().equals(name) && u.getId().equals(id)) {

					deleteUserInDataUsers(u); // update the database Users

					System.out.print("Utente identificato:");
					System.out.println(u.toString());
					System.out.println("Eliminato con successo");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Removes a specific operator from the global operator list. Performs safety
	 * checks to prevent manipulation of empty lists.
	 * 
	 * @param o the operator to remove from the system
	 */
	private void deleteUserInDataUsers(User u) {
		try {
			ActionOnUseRS.getInstance().deleteUser(u);

		} catch (Error e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private String userNameControl() {
		Checks k = Checks.neutral;
		String name = "";
		String a = "";

		/* chiediamo il nome */
		try {
			do {
				do { /*
						 * si inserisce un nome e si verifica se questo è corretto, quindi se è dento la
						 * lista operatori
						 */

					k = Checks.neutral;
					System.out.println("Che utente intedi eliminare?");

					List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

					/* rimuovo ROOT in modo sicuro */
					if (userList != null) {
						Iterator<User> it = userList.iterator();
						while (it.hasNext()) {
							User u = it.next();
							if (u.getName().equalsIgnoreCase(StrangeValues.ROOT.toString())) {
								it.remove();
							}
						}
					}

					/* se non ci sono utenti oltre root, esco */
					if (userList == null || userList.isEmpty()) {
						System.out.println("Nessun utente da eliminare (solo utente root presente).");
						return "";
					}

					/* stampo gli utenti rimasti */
					for (User u : userList) {
						System.out.println("- " + u.getName());
					}

					name = GlobalScaner.scanner.nextLine();
					Exit.exit(name);

					for (User u : userList) {
						if (u.getName().equals(name)) {
							k = Checks.ok;
						}
					}
					if (!k.equals(Checks.ok)) {
						System.out.println("Nome errato o non esistenete");
					}

				} while (!k.equals(Checks.ok));

				do { /*
						 * qui verifico se il nome (corretto) sia quello effettivamente desiderato, se
						 * non lo è si ricomincia con l'inserimento nome verificato
						 */
					a = "";
					System.out.println(name + " è l'utente corretto che vuoi eliminare? [s|n]");
					String r = GlobalScaner.scanner.nextLine();
					Exit.exit(r);
					a = r;
				} while (!(a.equals("s") | a.equals("n")));

				if (a.equals("s")) {
					System.out.println("");
				}
			} while (a.equals("n"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private String userIdControl(String name) {
		String id = "";
		String k = "";
		try {

			List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

			do {
				k = "";
				System.out.println("Conosci già l'id dell'utente? [s|n]");
				String r = GlobalScaner.scanner.nextLine();
				Exit.exit(r);
				k = r;
			} while (!(k.equals("s") | k.equals("n")));
			if (k.equals("n")) {
				System.out.println("Ecco descrizione Utente|Utenti " + name + ":\n");
				for (User u : userList) {
					if (u.getName().equals(name)) {
						System.out.println(u.toString());
					}
				}

				id = checkDeleteId(userList);

			} else if (k.equals("s")) {

				id = checkDeleteId(userList);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	private String checkDeleteId(List<User> userList) {
		Checks k = Checks.neutral;
		String id = "";
		try {
			do {

				k = Checks.neutral;

				System.out.println("Inserisci l'id utente per completare l'eliminazione");
				id = GlobalScaner.scanner.nextLine();
				Exit.exit(id);

				for (User u : userList) {
					if (u.getId().equals(id)) {
						k = Checks.ok;
					}
				}
				if (!k.equals(Checks.ok)) {
					System.out.println("Errore inserimento id, riprova");
				}

			} while (!k.equals(Checks.ok));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;

	}

}
