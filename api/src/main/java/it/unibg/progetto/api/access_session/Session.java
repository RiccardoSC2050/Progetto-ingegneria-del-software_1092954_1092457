package it.unibg.progetto.api.access_session;

import java.time.Instant;

import it.unibg.progetto.api.operators.AccessLevel;

public final class Session {

	private final String uuid;
	private final int accessLevel;
	private final Instant loginTime = Instant.now();

	public Session(String uuid, int accessLevel) {
		this.uuid = uuid;
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

	
}
