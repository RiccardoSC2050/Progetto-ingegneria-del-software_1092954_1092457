package it.unibg.progetto.api.application.dto;
import it.unibg.progetto.api.domain.rules.AccessLevel;

public class Rootdto {

	private String uuid;
	private String username;
	private String password;
	private int accessLevel;

	public Rootdto() {
		super();
	}

	public Rootdto(String uuid, String username, String password, AccessLevel accessLevel) {
		super();
		this.uuid = uuid;
		this.username = username;
		this.password = password;
		this.accessLevel = accessLevel.getLevel();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

}
