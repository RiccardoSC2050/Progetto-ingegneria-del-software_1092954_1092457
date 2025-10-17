package operators;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

abstract class Operator {

	private String name;
	private String password;
	private int accessLevel;
	private String id;
	private static List<Operator> allOperators = new ArrayList<>();

	/**
	 * 
	 * this constructor is used by Root to create an user, but not its self there
	 * are 3 access level
	 * 
	 * @param name
	 * @param password
	 * @param accessLevel
	 */
	public Operator(String name, String password, int accessLevel) {

		if (accessLevel < 0 || accessLevel > 3) {

			throw new IllegalArgumentException(
					"impossibile creare un utente con livello accesso negativo o superiore a 3");
		}

		this.name = name;
		this.password = password;
		this.accessLevel = accessLevel;
		this.id = UUID.randomUUID().toString().substring(0, 8);

		allOperators.add(this);
	}

	/**
	 * this is the Root's constructor, where it has an id and accessLevel as
	 * constant.
	 * 
	 * 
	 * @param name
	 * @param password
	 */
	public Operator(String name, String password) {
		this.name = name;
		this.password = password;
		this.id = "0";
		this.accessLevel = 5;

		allOperators.add(this);
	}

	/**
	 * 
	 * this method compare name and password inserted within the created list of all
	 * Operators
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public boolean login(String name, String password) {

		for (Operator op : allOperators) {
			if (op.getName() == name && op.getPassword() == password) {
				return true;
			}
		}
		return false;
	}

	/**
	 * this method disconnects the operator
	 */
	public boolean logout() {

		System.out.println("utente disconnesso");
		return true;
	}

	@Override
	public String toString() {
		return "Operator [name=" + name + ", accessLevel=" + accessLevel + ", id=" + id + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static List<Operator> getAllOperators() {
		return allOperators;
	}

	public static void deleteSpecificOperatorFromAllOperators(Operator o) {
		try {
			if(!allOperators.isEmpty()) {
		allOperators.remove(o);}
			else {
				System.out.println("Impossibile manomettere lista operatori, non ce ne sono...");
			}
		} catch (Error e) {
			System.out.println(e);
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * this method will use if someone knows exactly their UUID;
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

}
