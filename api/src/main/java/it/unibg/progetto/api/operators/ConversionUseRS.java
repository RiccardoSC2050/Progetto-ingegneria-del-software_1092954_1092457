package it.unibg.progetto.api.operators;

import java.util.List;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.stereotype.Component;

import it.unibg.progetto.api.dto.Userdto;
import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

@Component
public class ConversionUseRS {
	
	
	
	public ConversionUseRS() {
	}

	public List<User> convertListUsersIntoListUser(UsersService service, UserMapper userMapper) throws InvalidAccessLevelException{
	
	List<Users> UsersListFromDataUsers = service.getAllUsersFromDataBase();
	if (!UsersListFromDataUsers.isEmpty()) {
	List<User> userList = userMapper.getAllUsersInUserFormat(UsersListFromDataUsers);
	
	return userList;
	}
	return null;
	}
	
	public Users convertUserIntoUsersEntity( User u, UserMapper userMapper) {
		Userdto userdto = userMapper.toUserdtoFromUser(u.getId(), u.getName(), u.getPassword(), u.getAccessLevel());
		Users users = userMapper.toEntityUsersFromUserdto(userdto);
		return users;
	}
}
