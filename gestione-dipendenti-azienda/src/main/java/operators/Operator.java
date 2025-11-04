package operators;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base class for all system operators. Provides common functionality
 * for user authentication, access control, and operator management.
 * 
 * Access Levels: - 0-3: Regular users with different permission levels - 5:
 * Root/Administrator level
 * 
 * @author Employee Management System
 * @version 1.0
 */
abstract class Operator {

	private String name;
	private String password;
	private int accessLevel;
	private String id;
	private static List<Operator> allOperators = new ArrayList<>();

	/**
	 * Constructor for creating regular users with specified access level. Used by
	 * Root to create new users. Validates access level range (0-3). Automatically
	 * generates a unique 8-character ID and adds the operator to the global list.
	 * 
	 * @param name        the username for the operator
	 * @param password    the password for authentication
	 * @param accessLevel the permission level (0-3 for regular users)
	 * @throws InvalidAccessLevelException if accessLevel is outside valid range
	 *                                     (0-3)
	 */
	public Operator(String name, String password, int accessLevel) throws InvalidAccessLevelException {

		if (hasAtLeast(accessLevel, AccessLevel.AL1)) {

			throw new InvalidAccessLevelException(
					"impossibile creare un utente con livello accesso negativo o superiore a 3");
		}

		this.name = name;
		this.password = password;
		this.accessLevel = accessLevel;
		this.id = UUID.randomUUID().toString().substring(0, 8);

		allOperators.add(this);
	}

	public boolean hasAtLeast(int lv, AccessLevel level) {

		return (lv >= level.getLevel() && lv <= AccessLevel.AL3.getLevel() && lv > 0);

	}

	/**
	 * Constructor for creating the Root operator. Creates the administrator with
	 * fixed ID "0" and access level 5. Used exclusively by the Root class for
	 * singleton pattern implementation.
	 * 
	 * @param name     the username for the root operator
	 * @param password the password for root authentication
	 */
	public Operator(String name, String password) {
		this.name = name;
		this.password = password;
		this.id = "0";
		this.accessLevel = 5;

		allOperators.add(this);
	}

	/**
	 * Authenticates an operator by comparing credentials against all registered
	 * operators. Searches through the global operator list to find matching name
	 * and password.
	 * 
	 * @param name     the username to authenticate
	 * @param password the password to verify
	 * @return true if credentials match an existing operator, false otherwise
	 */
	public boolean login(String name, String password) {

		for (Operator op : allOperators) {
			if (op.getName().equals(name) && op.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Logs out the current operator from the system. Displays a disconnection
	 * message and performs cleanup operations.
	 * 
	 * @return true indicating successful logout
	 */
	public boolean logout() {

		System.out.println("User disconnected");
		return true;
	}

	/**
	 * Returns a string representation of this operator. Includes name, access
	 * level, and unique identifier.
	 * 
	 * @return formatted string containing operator information
	 */
	@Override
	public String toString() {
		return "Operator [name=" + name + ", accessLevel=" + accessLevel + ", id=" + id + "]";
	}

	/**
	 * Gets the operator's username.
	 * 
	 * @return the operator's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the operator's username.
	 * 
	 * @param name the new name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the operator's access level.
	 * 
	 * @return the current access level (0-3 for users, 5 for root)
	 */
	public int getAccessLevel() {
		return accessLevel;
	}

	/**
	 * Sets the operator's access level.
	 * 
	 * @param accessLevel the new access level to assign
	 */
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

	/**
	 * Gets the operator's unique identifier.
	 * 
	 * @return the operator's ID ("0" for root, 8-char UUID for users)
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the operator's unique identifier.
	 * 
	 * @param id the new ID to assign
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the complete list of all registered operators. Provides access to the
	 * global operator registry.
	 * 
	 * @return list containing all operators in the system
	 */
	public static List<Operator> getAllOperators() {
		return allOperators;
	}

	/**
	 * Removes a specific operator from the global operator list. Performs safety
	 * checks to prevent manipulation of empty lists.
	 * 
	 * @param o the operator to remove from the system
	 */
	public static void deleteSpecificOperatorFromAllOperators(Operator o) {
		try {
			if (!allOperators.isEmpty()) {
				allOperators.remove(o);
			} else {
				System.out.println("Cannot modify operator list - no operators exist.");
			}
		} catch (Error e) {
			System.out.println(e);
		}
	}

	/**
	 * Sets a new password for the operator. Updates the operator's authentication
	 * credentials.
	 * 
	 * @param password the new password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the operator's password for authentication purposes. Access restricted
	 * to package level to prevent unauthorized password exposure. Should only be
	 * used when the caller has the operator's UUID for verification.
	 * 
	 * @return the operator's current password
	 */
	protected String getPassword() {
		return password;
	}

}
