package it.unibg.progetto.api.cli.components;

public class CheckLenght {
	
	public static boolean checkLenghtPw(String p) {
		if (p.length() < 8)
			return false;
		return true;
	}

}
