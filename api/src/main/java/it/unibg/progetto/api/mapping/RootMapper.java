package it.unibg.progetto.api.mapping;


import org.springframework.stereotype.Component;

import it.unibg.progetto.api.application.dto.RootDto;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.data.Users;

@Component
public class RootMapper {

	// Root -> userdto -> users
	// users -> userdto -> Root

	public RootDto toRootdtoFromRoot(String id, String name, String pw, AccessLevel al) {
		return new RootDto(id, name, pw, al);
	}

	public Users toUsersfromRootdto(RootDto rootdto) {
		if (rootdto == null)
			return null;
		return new Users(rootdto.getUuid(), rootdto.getUsername(), rootdto.getPassword(), rootdto.getAccessLevel());
	}

	public RootDto fromUsers(Users u) {
		if (u == null)
			return null;
		return new RootDto(u.getUuid(), u.getName(), u.getPassword(), AccessLevel.fromLevel(u.getAccessLevel()));
	}

	public RootDto fromUser(User u) {
		if (u == null)
			return null;
		RootDto root = new RootDto(u.getId(), u.getName(), u.getPassword(), u.getAccessLevel());
		return root;
	}

}
