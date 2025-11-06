package operators;

public enum AccessLevel {
	AL1(1), AL2(2), AL3(3);

	private final int level;

	private AccessLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * print each level
	 */
	public void printAccessLevel() {
		for (AccessLevel accessLevel : AccessLevel.values()) {
			System.out.println(accessLevel + ": " + accessLevel.getLevel());
		}
	}
	
	

}
