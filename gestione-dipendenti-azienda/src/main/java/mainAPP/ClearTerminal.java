package mainAPP;

public class ClearTerminal {

	/**
	 * this method clear the terminal;
	 */
	public void clearTerminal() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

}
