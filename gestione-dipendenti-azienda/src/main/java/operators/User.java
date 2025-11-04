package operators;

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
	public User(String name, String password, int accessLevel) throws InvalidAccessLevelException {
		super(name, password, accessLevel);
	}


	/**
	 * Reads data from the user's associated data file. Implementation pending -
	 * will load user-specific data from storage.
	 */
	@Override
	public void readDataFile() {
		
		if(hasAtLeast(getAccessLevel(), AccessLevel.AL3)) {
			//method for level 3
		}
		else {
			//method for level 1-2
		}
		
	}

	/**
	 * Creates a new data file for storing user information. Implementation pending
	 * - will initialize user data storage.
	 */
	@Override
	public void createDataFile() {
		if(hasAtLeast(getAccessLevel(), AccessLevel.AL2)) {
			//method for level 2-3
		}
		else {
			//error
		}
	}

	/**
	 * Deletes the user's data file from storage. Implementation pending - will
	 * permanently remove user data.
	 */
	@Override
	public void deleteDataFile() {
		if(hasAtLeast(getAccessLevel(), AccessLevel.AL2)) {
			//method for level 2-3
		}
		else {
			//error
		}
	}
}
