package it.unibg.progetto.data;

import java.security.PrivateKey;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Users {
	@Id
	private String uuid;
	private String name;
	private String password;
	private int accessLevel;

	public Users() {
	}

	public Users(String uuid, String name, String password, int accessLevel) {
		this.uuid = uuid;
		this.name = name;
		this.password = password;
		this.accessLevel = accessLevel;
	}

	public String getUui() {
		return uuid;
	}

	public void setUui(String uui) {
		this.uuid = uui;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
