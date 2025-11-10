package it.unibg.progetto.api.action_on;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.atn.SemanticContext.Operator;
import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.boot.jdbc.HikariCheckpointRestoreLifecycle;
import org.springframework.stereotype.Component;

import ch.qos.logback.core.net.LoginAuthenticator;
import it.unibg.progetto.api.dto.Userdto;
import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.api.operators.InvalidAccessLevelException;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.api.operators.User;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

@Component
public class ActionOnUseRS {

	private final UserMapper userMapper;
	private final UsersService usersService;

	public ActionOnUseRS(UserMapper userMapper, UsersService usersService) {

		this.userMapper = userMapper;
		this.usersService = usersService;
	}
//conversion from Users to User
	
	/**
	 * 
	 * @param usersService
	 * @param userMapper
	 * @return a List of User without password, to protect their account
	 * @throws InvalidAccessLevelException
	 */
	private List<User> converterListUsersToListUserProtected(UsersService usersService, UserMapper userMapper)
			throws InvalidAccessLevelException {

		List<Users> UsersListFromDataUsers = usersService.getAllUsersFromDataBase();
		if (!UsersListFromDataUsers.isEmpty()) {
			List<User> userList = userMapper.getAllUsersInUserFormatWithoutPassword(UsersListFromDataUsers);

			return userList;
		}

		return null;
	}
	
	
	

	/**
	 * 
	 * @return an List of User with out password
	 * @throws InvalidAccessLevelException
	 */
	public List<User> trasformListUsersIntoListUserWithoutPassword() throws InvalidAccessLevelException {

		List<User> userList = converterListUsersToListUserProtected(usersService, userMapper);
		return userList;
	}

	private List<User> converterListUsersToListUserNotProtected(UsersService usersService, UserMapper userMapper)
			throws InvalidAccessLevelException {
		
		List<Users> UsersListFromDataUsers = usersService.getAllUsersFromDataBase();
		if (!UsersListFromDataUsers.isEmpty()) {
			List<User> userList = userMapper.getAllUsersInUserFormat(UsersListFromDataUsers);
			
			return userList;
		}
		
		return null;
	}
	
	private List<User> trasformListUsersIntoListUser() throws InvalidAccessLevelException {
		
		List<User> userList = converterListUsersToListUserNotProtected(usersService, userMapper);
		return userList;
	}
	
//conversion from User to Users
	/**
	 * 
	 * @param u
	 * @param userMapper
	 * @return
	 */
	private Users converterUserToUsersEntity(User u, UserMapper userMapper) {
		Userdto userdto = userMapper.toUserdtoFromUser(u.getId(), u.getName(), u.getPassword(), u.getAccessLevel());
		Users users = userMapper.toEntityUsersFromUserdto(userdto);
		return users;
	}

	public Users trasformUserIntoUsersEntity(User u) {
		Users users = converterUserToUsersEntity(u, userMapper);
		return users;
	}

//method to manage database Users
	/**
	 * 
	 * @param u
	 */
	public void addUserOnData(User u) {
		Users users = trasformUserIntoUsersEntity(u);
		usersService.addUsersIntoDataUsers(users);

	}

	/**
	 * 
	 * @param u
	 */
	public void deleteUser(User u) {
		Users users = trasformUserIntoUsersEntity(u);
		usersService.deleteUsers(users);
	}
	
	public boolean LoginAuthenticator(String name, String pw) throws InvalidAccessLevelException {
		List<User> userList = new ArrayList<>();
		userList=trasformListUsersIntoListUser();
		
		for(User u : userList) {
			if(u.getName().equals(name) && u.getPassword().equals(pw)) {
				return true;
			}
		}
		return false;
	}

}
