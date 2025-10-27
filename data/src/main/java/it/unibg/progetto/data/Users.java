package it.unibg.progetto.data;

import java.security.PrivateKey;

import org.hibernate.dialect.type.SQLServerCastingXmlArrayJdbcType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Users {
	@Id
	private String uui;
	private String name;
	private String password;

	public Users() {
	}

	public Users(String name, String password) {
		this.name = name;
		this.password = password;
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

}
