package operators;

import mainAPP.GlobalScaner;
import mainAPP.Quit;

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
	public Root(String name, String password) {
		super(name, password);
	}

	/**
	 * this method use the static root to create a unique instance of root; used in
	 * main
	 * 
	 * @return Root instance
	 */
	public static Root getInstanceRoot() {
		try {
			if (root == null) {
				root = new Root("ROOT", "1234");
			}
		} catch (Exception e) {
			System.err.println("Error creating Root instance: " + e.getMessage());
		}
		return root;
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
	public void createUser(String name, String pw, int al) throws InvalidAccessLevelException {
		try {
			User user = new User(name, pw, al);
			System.out.println("utente creato con successo:");
			System.out.println(user.toString());
		} catch (ExceptionInInitializerError e) {
			System.out.println("ERRORE: non è stato possibile creare l'utente");
		}
	}

	/**
	 * the main method to delete user
	 */
	public void deleteUser() {

		String n = userNameControl();
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
			for (Operator o : getAllOperators()) {
				if (o.getName().equals(name) && o.getId().equals(id)) {
					deleteSpecificOperatorFromAllOperators(o);
					System.out.print("Utente identificato:");
					System.out.println(o.toString());
					System.out.println("eliminato con successo");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private String userNameControl() {
		String k = "";
		String name = "";

		/* chiediamo il nome */
		try {
			do {
				do { /*
						 * si inserisce un nome e si verifica se questo è corretto, quindi se è dento la
						 * lista operatori
						 */
					k = "";
					System.out.println("che utente intedi eliminare?");
					for (Operator o : getAllOperators()) {
						System.out.println("- " + o.getName());
					}
					name = GlobalScaner.scanner.nextLine();
					Quit.quit(name);

					for (Operator o : getAllOperators()) {
						if (o.getName().equals(name)) {
							k = "ok";
						}
					}
					if (!k.equals("ok")) {
						System.out.println("nome errato o non esistenete");
					}

				} while (!k.equals("ok"));

				do { /*
						 * qui verifico se il nome (corretto) sia quello effettivamente desiderato, se
						 * non lo è si ricomincia con l'inserimento nome verificato
						 */
					System.out.println(name + " è l'utente corretto che vuoi eliminare? [y|n]");
					String r = GlobalScaner.scanner.nextLine();
					Quit.quit(r);
					k = r;
				} while (!(k.equals("y") | k.equals("n")));

				if (k.equals("y")) {
					System.out.println("");
				}
			} while (k.equals("n"));

		} catch (Exception e) {
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
			do {

				System.out.println("conosci già l'id dell'utente? [y|n]");
				String r = GlobalScaner.scanner.nextLine();
				Quit.quit(r);
				k = r;
			} while (!(k.equals("y") | k.equals("n")));
			if (k.equals("n")) {
				System.out.println("ecco descrizione Utente|Utenti " + name + ":\n");
				for (Operator o : getAllOperators()) {
					if (o.getName().equals(name)) {
						System.out.println(o.toString());
					}
				}

				id = checkDeleteId();

			} else if (k.equals("y")) {

				id = checkDeleteId();

			}
		} catch (Exception e) {
		}
		return id;
	}

	private String checkDeleteId() {
		String k;
		String id = "";
		try {
			do {
				k = "";
				System.out.println("Inserisci l'id utente per completare l'eliminazione");
				id = GlobalScaner.scanner.nextLine();
				Quit.quit(id);

				for (Operator o : getAllOperators()) {
					if (o.getId().equals(id)) {
						k = "ok";
					}
				}
				if (!k.equals("ok")) {
					System.out.println("Errore inserimento id, riprova");
				}

			} while (!k.equals("ok"));
		} catch (Exception e) {
		}
		return id;

	}

}
