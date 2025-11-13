package it.unibg.progetto.api.components;

/**
 * Utility class for terminal operations. Provides functionality to clear the
 * console screen using ANSI escape sequences.
 * 
 * @author Employee Management System
 * @version 1.0
 */
public class ClearTerminal {

	/**
	 * Clears the terminal screen using ANSI escape codes. Uses escape sequence to
	 * move cursor to home position and clear display. Compatible with most modern
	 * terminals that support ANSI codes.
	 */
	public static void clearTerminal(String clear) {
		if (clear.equals("clear")) {
			System.out.print("\033[H\033[2J"); // ANSI escape codes: H=home, 2J=clear screen
			System.out.flush(); // Force output to be written immediately
		}
	}
}
