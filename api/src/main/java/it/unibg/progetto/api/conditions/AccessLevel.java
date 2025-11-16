package it.unibg.progetto.api.conditions;

public enum AccessLevel {
	AL1(1), AL2(2), AL3(3);

	private final int level;

	AccessLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
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
