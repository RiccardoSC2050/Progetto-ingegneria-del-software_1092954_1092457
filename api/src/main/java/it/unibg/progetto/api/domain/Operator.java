package it.unibg.progetto.api.domain;

import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.StrangeValues;

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
	private int accessLevelValue;
	private AccessLevel accessLevel;
	private String id;

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
	public Operator(String name, String password, AccessLevel accessLevel) {
		this.name = name.toLowerCase();
		this.password = password;
		this.accessLevel = accessLevel;
		this.id = java.util.UUID.randomUUID().toString().substring(0, 8);

		if (accessLevel != null) {
			this.accessLevelValue = accessLevel.getLevel();
		} else {
			this.accessLevelValue = AccessLevel.AL1.getLevel();
		}
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param password
	 * @param accessLevel this method is used by userdto to convert users in user
	 */
	public Operator(String id, String name, String password, AccessLevel accessLevel) {
		this.id = id;
		this.name = name.toLowerCase();
		this.password = password;
		this.accessLevel = accessLevel;

		if (accessLevel != null) {
			this.accessLevelValue = accessLevel.getLevel();
		} else {
			// caso "speciale" (es. utente secret): nessun livello definito
			this.accessLevelValue = AccessLevel.AL1.getLevel(); // o un valore neutro a tua scelta
		}
	}

	/**
	 * Constructor for creating the Root operator. Creates the administrator with
	 * fixed ID "0" and access level 5. Used exclusively by the Root class for
	 * singleton pattern implementation.
	 * 
	 * @param name     the username for the root operator
	 * @param password the password for root authentication
	 */
	public Operator(String password) {
		this.name = String.valueOf(StrangeValues.ROOT);
		this.password = password;
		this.id = String.valueOf(StrangeValues.ROOTid.getLevel());
		this.accessLevelValue = AccessLevel.AL5.getLevel();

	}

	/**
	 * Returns a string representation of this operator. Includes name, access
	 * level, and unique identifier.
	 * 
	 * @return formatted string containing operator information
	 */
	@Override
	public String toString() {
		return "Operatore [name=" + name + ", accessLevelValue=" + accessLevelValue + ", id=" + id + "]";
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
	 * 
	 * @return
	 */
	public int getAccessLevelValue() {
		return accessLevelValue;
	}

	/**
	 * 
	 * @param accessLevelValue
	 */
	public void setAccessLevelValue(int accessLevelValue) {
		this.accessLevelValue = accessLevelValue;
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
	public String getPassword() {
		return password;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

}