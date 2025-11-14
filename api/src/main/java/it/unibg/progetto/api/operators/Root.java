package it.unibg.progetto.api.operators;

import java.security.PrivateKey;
import java.security.Provider.Service;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;

import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.components.Exit;
import it.unibg.progetto.api.dto.Userdto;
import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

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
	private boolean isPossibleTocreateUser(String name) {
		try {

			List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();
			if(userList!=null) {
			for (User u : userList) {
				if (u.getName().equals(name)) {

					System.err.println("ERRORE di inserimento: il nome " + name
							+ " utente già esiste, si prega di inserirne uno nuovo");
					return false;
				}
			}
			}
			System.out.println("inserire password utente: ");
			String pw = GlobalScaner.scanner.nextLine();
			int aclv;
			do {
				System.out.println("inserire il livello di accesso utente [1-3]: ");
				String al = GlobalScaner.scanner.nextLine();
				aclv = Integer.parseInt(al);

			} while (!(aclv > 0 && aclv <= 3));

			AccessLevel alv = AccessLevel.fromLevel(aclv);

			User user = new User(name, pw, alv);
			ActionOnUseRS.getInstance().addUserOnData(user);

			System.out.println("utente creato con successo:");
			System.out.println(user.toString());

		} catch (ExceptionInInitializerError e) {
			System.out.println("ERRORE: non è stato possibile creare l'utente");
		}
		return true;
	}

	public void createUser() {
		boolean action = false;
		do {

			System.out.println("inserire nome utente: ");
			String name = GlobalScaner.scanner.nextLine().toLowerCase();

			action = isPossibleTocreateUser(name);

		} while (!action);

	}

	/**
	 * the main method to delete user
	 * 
	 * @throws InvalidAccessLevelException
	 */
	public void deleteUser() {

		List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

		if (userList == null) {
			System.out.println("nessun utente da eliminare");
			return;
		}

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

			name = name.toLowerCase();
			List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

			for (User u : userList) {
				if (u.getName().equals(name) && u.getId().equals(id)) {

					deleteUserInDataUsers(u); // update the database Users

					System.out.print("Utente identificato:");
					System.out.println(u.toString());
					System.out.println("eliminato con successo");
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

					List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

					for (User u : userList) {
						System.out.println("- " + u.getName());
					}
					name = GlobalScaner.scanner.nextLine();
					Exit.exit(name);

					for (User u : userList) {
						if (u.getName().equals(name)) {
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
					Exit.exit(r);
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

			List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();

			do {

				System.out.println("conosci già l'id dell'utente? [y|n]");
				String r = GlobalScaner.scanner.nextLine();
				Exit.exit(r);
				k = r;
			} while (!(k.equals("y") | k.equals("n")));
			if (k.equals("n")) {
				System.out.println("ecco descrizione Utente|Utenti " + name + ":\n");
				for (User u : userList) {
					if (u.getName().equals(name)) {
						System.out.println(u.toString());
					}
				}

				id = checkDeleteId(userList);

			} else if (k.equals("y")) {

				id = checkDeleteId(userList);

			}
		} catch (Exception e) {
		}
		return id;
	}

	private String checkDeleteId(List<User> userList) {
		String k;
		String id = "";
		try {
			do {
				k = "";
				System.out.println("Inserisci l'id utente per completare l'eliminazione");
				id = GlobalScaner.scanner.nextLine();
				Exit.exit(id);

				for (User u : userList) {
					if (u.getId().equals(id)) {
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
