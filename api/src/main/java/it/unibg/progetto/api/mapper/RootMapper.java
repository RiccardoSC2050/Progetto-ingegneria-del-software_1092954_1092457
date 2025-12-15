package it.unibg.progetto.api.mapper;

import org.springframework.stereotype.Component;

import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.dto.Rootdto;

import it.unibg.progetto.api.operators.User;
import it.unibg.progetto.data.Users;

@Component
public class RootMapper {

	// Root -> userdto -> users
	// users -> userdto -> Root

	public Rootdto toRootdtoFromRoot(String id, String name, String pw, AccessLevel al) {
		return new Rootdto(id, name, pw, al);
	}

	public Users toUsersfromRootdto(Rootdto rootdto) {
		if (rootdto == null)
			return null;
		return new Users(rootdto.getUuid(), rootdto.getUsername(), rootdto.getPassword(), rootdto.getAccessLevel());
	}

	public Rootdto fromUsers(Users u) {
		if (u == null)
			return null;
		return new Rootdto(u.getUuid(), u.getName(), u.getPassword(), AccessLevel.fromLevel(u.getAccessLevel()));
	}

	public Rootdto fromUser(User u) {
		if (u == null)
			return null;
		Rootdto root = new Rootdto(u.getId(), u.getName(), u.getPassword(), u.getAccessLevel());
		return root;
	}

}
