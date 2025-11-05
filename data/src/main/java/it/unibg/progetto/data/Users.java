package it.unibg.progetto.data;

import java.security.PrivateKey;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Users {
	@Id
	private String uui;
	private String name;
	private String password;
	private String accessLevel;

	public Users() {
	}

	public Users(String name, String password, String accessLevel) {
		this.name = name;
		this.password = password;
		this.accessLevel = accessLevel;
	}

	public String getUui() {
		return uui;
	}

	public void setUui(String uui) {
		this.uui = uui;
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

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	
}
