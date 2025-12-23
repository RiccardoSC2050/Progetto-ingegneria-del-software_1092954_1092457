package it.unibg.progetto.api.mapping;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.unibg.progetto.api.application.dto.UserDto;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.data.Users;

@Component
public class UserMapper {

	// user -> userdto -> users
	// users -> userdto -> user

	/**
	 * 
	 * @param id
	 * @param name
	 * @param pw
	 * @param al
	 * @return
	 */
	public UserDto toUserdtoFromUser(String id, String name, String pw, AccessLevel al) {
		return new UserDto(id, name, pw, al);
	}

	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public Users toEntityUsersFromUserdto(UserDto userDTO) {
		if (userDTO == null)
			return null;
		return new Users(userDTO.getUuid(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getAccessLevel());
	}

	/**
	 * 
	 * @param users
	 * @return
	 */
	public UserDto toUserdtoFromUsers(Users users) {
		if (users == null)
			return null;
		return new UserDto(users.getUuid(), users.getName(), users.getPassword(),
				AccessLevel.fromLevel(users.getAccessLevel()));
	}

	/**
	 * 
	 * @param userdto
	 * @return
	 * @throws InvalidAccessLevelException
	 */
	public User toUserFromUserDTO(UserDto userdto) {
		if (userdto == null)
			return null;
		return new User(userdto.getUuid(), userdto.getUsername(), userdto.getPassword(), userdto.getAccessLevelvalue());

	}

	/**
	 * 
	 * @param usersList
	 * @return
	 * @throws InvalidAccessLevelException
	 */
	public List<User> getAllUsersInUserFormatWithoutPassword(List<Users> usersList) {
		List<User> userList = new ArrayList<>();

		if (!usersList.isEmpty()) {
			for (Users u : usersList) {
				UserDto userdto = toUserdtoFromUser(u.getUuid(), u.getName(), "*********",
						AccessLevel.fromLevel(u.getAccessLevel()));
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
	public List<User> getAllUsersInUserFormat(List<Users> usersList) {
		List<User> userList = new ArrayList<>();

		if (!usersList.isEmpty()) {
			for (Users u : usersList) {
				UserDto userdto = toUserdtoFromUser(u.getUuid(), u.getName(), u.getPassword(),
						AccessLevel.fromLevel(u.getAccessLevel()));
				User user = toUserFromUserDTO(userdto);
				userList.add(user);
			}
			return userList;
		}
		return null;
	}
	

}
