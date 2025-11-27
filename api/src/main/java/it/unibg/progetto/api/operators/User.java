package it.unibg.progetto.api.operators;

import it.unibg.progetto.api.conditions.AccessLevel;

/**
 * Regular user implementation extending the Operator base class. Provides
 * standard user functionality with access level validation (0-3). Implements
 * DataControl interface for file management operations.
 * 
 * @author Employee Management System
 * @version 1.0
 */
public class User extends Operator implements DataControl {

	/**
	 * Constructs a new User with specified credentials and access level. Validates
	 * that the access level is within the valid range for regular users.
	 * 
	 * @param name        the username for the user
	 * @param password    the password for authentication
	 * @param accessLevel the permission level (must be 0-3)
	 * @throws InvalidAccessLevelException if accessLevel is outside valid range
	 */
	public User(String name, String password, AccessLevel accessLevel) {
		super(name, password, accessLevel);
		
	}


	/**
	 * 
	 * @param id
	 * @param name
	 * @param password
	 * @param accessLevel
	 * @throws InvalidAccessLevelException
	 */
	public User(String id, String name, String password, AccessLevel accessLevel) {
		super(id, name, password, accessLevel);
		// TODO Auto-generated constructor stub
	}




	/**
	 * Reads data from the user's associated data file. Implementation pending -
	 * will load user-specific data from storage.
	 */
	@Override
	public void readDataFile() {

	}


	/**
	 * Creates a new data file for storing user information. Implementation pending
	 * - will initialize user data storage.
	 */
	@Override
	public void createDataFile() {
		
	}

	/**
	 * Deletes the user's data file from storage. Implementation pending - will
	 * permanently remove user data.
	 */
	@Override
	public void deleteDataFile() {
		
	}
}
