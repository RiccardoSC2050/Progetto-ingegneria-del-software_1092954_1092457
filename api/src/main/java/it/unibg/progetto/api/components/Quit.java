package it.unibg.progetto.api.components;

/**
 * Utility class for handling application exit functionality.
 * Provides a centralized method to check for quit commands and terminate the application.
 * 
 * @author Employee Management System
 * @version 1.0
 */
public class Quit {

	/**
	 * Checks if the input string is a quit command and terminates the application if so.
	 * Recognizes "q" as the quit command and performs a clean application exit.
	 * 
	 * @param input the user input string to check for quit command
	 */
	public static void quit(String input) {
		if(input.equals("q")) {
			System.out.println("Operation cancelled - Application terminating");
			System.exit(0); // Terminate application with success code
		}
	}
}
