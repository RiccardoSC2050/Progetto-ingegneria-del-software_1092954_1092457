package mapper;



import org.springframework.stereotype.Component;

import dto.Userdto;
import it.unibg.progetto.data.Users;

@Component
public class UserMapper {
	
	
	public Userdto toUserdto(String id, String name, String pw, int al) {
		return new Userdto(id, name, pw, al);
	}
	
	public Users toEntityUsers(Userdto userDTO) {
		if (userDTO == null) return null;
		return new Users(userDTO.getUuid(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getAccessLevel());
	}
	
	public Userdto toUser (Users users) {
		if ( users == null) return null;
		return new Userdto(users.getUui(), users.getName(), users.getPassword(), users.getAccessLevel());
	}
	
	

}
