package it.unibg.progetto.data;

import java.security.PrivateKey;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
