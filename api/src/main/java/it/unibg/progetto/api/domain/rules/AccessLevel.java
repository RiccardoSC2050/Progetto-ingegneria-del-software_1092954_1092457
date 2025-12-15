package it.unibg.progetto.api.domain.rules;


public enum AccessLevel {
	AL1(1), AL2(2), AL3(3), AL5(5);

	private final int level;

	AccessLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public static boolean isAPossibleValue(int n) {

		return fromLevel(n) != null;
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static AccessLevel fromLevel(int i) {
		for (AccessLevel a : values()) {
			if (a.getLevel() == i) {
				return a;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	public static void printAllLevels() {
		for (AccessLevel a : values()) {
			System.out.println(a + ": " + a.getLevel());
		}
	}
}
