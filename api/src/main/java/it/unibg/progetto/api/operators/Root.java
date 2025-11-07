package it.unibg.progetto.api.operators;

import java.security.PrivateKey;
import java.security.Provider.Service;
import java.util.List;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;

import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.components.Quit;
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

	private final UserMapper userMapper;
	private final UsersService service;
	private final ConversionUseRS conversionUseRS;

	/**
	 * Constructs a Root operator with administrator privileges. Sets fixed ID "0"
	 * and access level 5 through parent constructor.
	 * 
	 * @param name     the username for the root administrator
	 * @param password the password for root authentication
	 */
	public Root(String name, String password, UserMapper userMapper, UsersService service,
			ConversionUseRS conversionUseRS) {
		super(name, password);
		this.userMapper = userMapper;
		this.service = service;
		this.conversionUseRS = conversionUseRS;
	}

	/**
	 * this method use the static root to create a unique instance of root; used in
	 * main
	 * 
	 * @return Root instance
	 */
	public static Root getInstanceRoot(UserMapper userMapper, UsersService service, ConversionUseRS conversionUseRS) {
		try {
			if (root == null) {
				root = new Root("ROOT", "1234", userMapper, service, conversionUseRS);
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
			Userdto userdto = userMapper.toUserdtoFromUser(user.getId(), user.getName(), user.getPassword(),
					user.getAccessLevel());
			Users usersData = userMapper.toEntityUsersFromUserdto(userdto);
			service.addUsersIntoDataUsers(usersData);

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

			List<User> userList = conversionUseRS.convertListUsersIntoListUser(service, userMapper);

			if (userList == null) {
				System.out.println("nessun utente da eliminare");
				return;
			}

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
			Users users = conversionUseRS.convertUserIntoUsersEntity(u, userMapper);
			service.deleteUsers(users);

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
					
					List<User> userList = conversionUseRS.convertListUsersIntoListUser(service, userMapper);
					
					//no one to delete
					if (userList == null) {
						return null;
					}
					
					for (User u : userList) {
						System.out.println("- " + u.getName());
					}
					name = GlobalScaner.scanner.nextLine();
					Quit.quit(name);

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
			
			if(name == null) {
				return null;
			}
			
			List<User> userList = conversionUseRS.convertListUsersIntoListUser(service, userMapper);
			
			do {

				System.out.println("conosci già l'id dell'utente? [y|n]");
				String r = GlobalScaner.scanner.nextLine();
				Quit.quit(r);
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
				Quit.quit(id);

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
