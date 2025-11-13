package it.unibg.progetto.api.components;

public class Quit {

	public static boolean quit(String input) {
		if (input.equals("q")) {
			System.out.println("Terminata esecuzione del comando");
			return true;
		}
		return false;
	}

}
