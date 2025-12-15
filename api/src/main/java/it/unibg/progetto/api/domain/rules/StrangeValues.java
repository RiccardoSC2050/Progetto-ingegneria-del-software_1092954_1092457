package it.unibg.progetto.api.domain.rules;


public enum StrangeValues {
	secret(0), ROOTid(0), ROOT(0);

	private final int level;

	StrangeValues(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
	
	
}
