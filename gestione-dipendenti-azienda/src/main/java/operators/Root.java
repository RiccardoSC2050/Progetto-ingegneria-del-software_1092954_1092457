package operators;

import java.util.Scanner;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import mainAPP.Quit;

public class Root extends Operator implements DataControl {

	private static Root root = null;

	/**
	 * root has id = 0 and access level = 5;
	 * 
	 * @param name
	 * @param password
	 */
	public Root(String name, String password) {
		super(name, password);

	}

	/**
	 * this method use the static root to create a unique instance of root; used in
	 * main
	 * 
	 * @return
	 */
	public static Root getIstanceRoot() {
		if (root == null) {
			root = new Root("ROOT", "1234");
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
	 */
	public void createUser(String name, String pw, int al) {
		try {
			User user = new User(name, pw, al);
			System.out.println("utente creato con successo:");
			user.toString();
		} catch (ExceptionInInitializerError e) {
			System.out.println("ERRORE: non è stato possibile creare l'utente");
		}
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
				if (o.getName() == name && o.getId() == id) {
					deleteSpecificOperatorFromAllOperators(o);
					System.out.print("Utente identificato:");
					o.toString();
					System.out.println("eliminato con successo");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private String userNameControl() {
		Scanner scanner = new Scanner(System.in);
		String k = "";
		String name;

		/* chiediamo il nome */
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
				name = scanner.nextLine();
				Quit.quit(name);

				for (Operator o : getAllOperators()) {
					if (o.getName() == name) {
						k = "ok";
					}
				}
				if (k != "ok") {
					System.out.println("nome errato o non esistenete");
				}

			} while (k != "ok");

			do { /*
					 * qui verifico se il nome (corretto) sia quello effettivamente desiderato, se
					 * non lo è si ricomincia con l'inserimento nome verificato
					 */
				System.out.println(name + " è l'utente corretto che vuoi eliminare? [y|n]");
				String r = scanner.nextLine();
				Quit.quit(r);
				k = r;
			} while (!(k == "y" | k == "n"));

			if (k == "n") {
				System.out.println("");
			}
		} while (k == "n");
		scanner.close();
		return name;
	}

	private String userIdControl(String name) {
		String id = "";
		Scanner scanner = new Scanner(System.in);
		String k = "";

		do {

			System.out.println("conosci già l'id dell'utente? [y|n]");
			String r = scanner.nextLine();
			Quit.quit(r);
			k = r;
		} while (!(k == "y" | k == "n"));
		if (k == "n") {
			System.out.println("ecco descrizione Utente|Utenti " + name + ":\n");
			for (Operator o : getAllOperators()) {
				if (o.getName() == name) {
					o.toString();
				}
			}

			id = checkDeleteId();

		} else if (k == "y") {

			id = checkDeleteId();

		}
		scanner.close();
		return id;
	}

	private String checkDeleteId() {
		Scanner scanner = new Scanner(System.in);
		String k;
		String id;
		do {
			k = "";
			System.out.println("Inserisci l'id utente per completare l'eliminazione");
			id = scanner.nextLine();
			Quit.quit(id);

			for (Operator o : getAllOperators()) {
				if (o.getId() == id) {
					k = "ok";
				}
			}
			if (k != "ok") {
				System.out.println("Errore inserimento id, riprova");
			}

		} while (!(k == "ok"));
		scanner.close();
		return id;

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
	public void deleteDAtaFile() {
		// TODO Auto-generated method stub

	}

}
