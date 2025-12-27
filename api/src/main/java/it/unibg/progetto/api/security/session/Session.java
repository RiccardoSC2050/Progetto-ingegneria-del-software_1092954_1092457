package it.unibg.progetto.api.security.session;


import java.time.Instant;



public final class Session {

	private final String uuid;
	private final String name;
	private final int accessLevel;
	private final Instant loginTime = Instant.now();

	public Session(String uuid, String name, int accessLevel) {
		this.uuid = uuid;
		this.name = name;
		this.accessLevel = accessLevel;

	}

	public String getUuid() {
		return uuid;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public Instant getLoginTime() {
		return loginTime;
	}

	public String getName() {
		return name;
	}

}
