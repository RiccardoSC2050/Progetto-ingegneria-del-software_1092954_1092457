package it.unibg.progetto.api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.unibg.progetto.api.dto.Userdto;
import it.unibg.progetto.api.operators.InvalidAccessLevelException;
import it.unibg.progetto.api.operators.User;
import it.unibg.progetto.data.Users;

@Component
public class UserMapper {

	//user -> userdto -> users
	//users -> userdto -> user
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param pw
	 * @param al
	 * @return
	 */
	public Userdto toUserdtoFromUser(String id, String name, String pw, int al) {
		return new Userdto(id, name, pw, al);
	}

	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public Users toEntityUsersFromUserdto(Userdto userDTO) {
		if (userDTO == null)
			return null;
		return new Users(userDTO.getUuid(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getAccessLevel());
	}

	/**
	 * 
	 * @param users
	 * @return
	 */
	public Userdto toUserdtoFromUsers(Users users) {
		if (users == null)
			return null;
		return new Userdto(users.getUuid(), users.getName(), users.getPassword(), users.getAccessLevel());
	}

	/**
	 * 
	 * @param userdto
	 * @return
	 * @throws InvalidAccessLevelException
	 */
	public User toUserFromUserDTO(Userdto userdto) throws InvalidAccessLevelException {
		if (userdto == null)
			return null;
		return new User(userdto.getUuid(), userdto.getUsername(), userdto.getPassword(), userdto.getAccessLevel());

	}

	/**
	 * 
	 * @param usersList
	 * @return
	 * @throws InvalidAccessLevelException
	 */
	public List<User> getAllUsersInUserFormatWithoutPassword(List<Users> usersList) throws InvalidAccessLevelException {
		List<User> userList = new ArrayList<>();

		if (!usersList.isEmpty()) {
			for (Users u : usersList) {
				Userdto userdto = toUserdtoFromUser(u.getUuid(), u.getName(), "*********", u.getAccessLevel());
				User user = toUserFromUserDTO(userdto);
				userList.add(user);
			}
			return userList;
		}
		return null;
	}
	/**
	 * 
	 * @param usersList
	 * @return
	 * @throws InvalidAccessLevelException
	 */
	public List<User> getAllUsersInUserFormat(List<Users> usersList) throws InvalidAccessLevelException {
		List<User> userList = new ArrayList<>();

		if (!usersList.isEmpty()) {
			for (Users u : usersList) {
				Userdto userdto = toUserdtoFromUser(u.getUuid(), u.getName(), u.getPassword(), u.getAccessLevel());
				User user = toUserFromUserDTO(userdto);
				userList.add(user);
			}
			return userList;
		}
		return null;
	}
	
	

}
