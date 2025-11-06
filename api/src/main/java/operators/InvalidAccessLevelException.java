package operators;

/**
 * Custom exception for invalid access level validation.
 * Thrown when attempting to create an operator with an access level outside the valid range.
 * 
 * Valid access levels:
 * - 0-3: Regular users with different permission levels
 * - 5: Root/Administrator level
 * 
 * @author Employee Management System
 * @version 1.0
 */
public class InvalidAccessLevelException extends Exception {

	/**
     * Constructs a new InvalidAccessLevelException with the specified detail message.
     * 
     * @param message the detail message explaining why the access level is invalid
     */
    public InvalidAccessLevelException(String message) {
        super(message); // Pass the message to the parent Exception class
    }
}
