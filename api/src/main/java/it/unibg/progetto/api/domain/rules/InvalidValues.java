package it.unibg.progetto.api.domain.rules;


public enum InvalidValues {
	secret(0), ROOTid(0), ROOT(0);

	private final int level;

	InvalidValues(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
	
	
}
