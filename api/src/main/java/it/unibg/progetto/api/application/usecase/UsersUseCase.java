package it.unibg.progetto.api.application.usecase;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import it.unibg.progetto.api.application.dto.RootDto;
import it.unibg.progetto.api.application.dto.UserDto;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.Validators;
import it.unibg.progetto.api.domain.rules.InvalidValues;
import it.unibg.progetto.api.mapping.RootMapper;
import it.unibg.progetto.api.mapping.UserMapper;
import it.unibg.progetto.api.security.Hash;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

@Component
public class UsersUseCase {

	private static UsersUseCase instance;
	private final UserMapper userMapper;
	private final UsersService usersService;
	private final RootMapper rootMapper;

	@Autowired
	public UsersUseCase(UserMapper userMapper, UsersService usersService, RootMapper rootMapper) {

		this.userMapper = userMapper;
		this.usersService = usersService;
		this.rootMapper = rootMapper;
		instance = this;
	}

	public static UsersUseCase getInstance() {
		return instance;
	}

	// ROOT

	public int numberOfAllOperators() {
		List<User> userList = trasformListUsersIntoListUserComplite();
		if (userList == null)
			return 0;
		int i = 0;
		for (User u : userList) {
			if (u != null)
				i++;
		}
		return i;

	}

	public RootDto rootIsOnData() {
		List<User> userList = trasformListUsersIntoListUserComplite();
		if (userList != null) {
			for (User u : userList) {
				if (u.getId().equals(String.valueOf(InvalidValues.ROOTid.getLevel()))
						// confronto case-insensitive, coerente con come salvi il root
						&& u.getName().equalsIgnoreCase(InvalidValues.ROOT.toString())
						&& u.getAccessLevelValue() == AccessLevel.AL5.getLevel()) {

					return rootMapper.fromUser(u);
				}
			}
		}
		return null;
	}

	public void addRootOnData(Root root) {
		RootDto rootdto = rootMapper.toRootdtoFromRoot(root.getId(), root.getName(), root.getPassword(),
				AccessLevel.fromLevel(root.getAccessLevelValue()));
		Users users = rootMapper.toUsersfromRootdto(rootdto);
		usersService.addUsersIntoDataUsers(users);

	}

//conversion from Users to User

	/**
	 * 
	 * @param usersService
	 * @param userMapper
	 * @return a List of User without password, to protect their account
	 * @throws InvalidAccessLevelException
	 */
	private List<User> converterListUsersToListUserProtected(UsersService usersService, UserMapper userMapper) {

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
	 * 
	 */
	public List<User> trasformListUsersIntoListUserWithoutPassword() {

		List<User> userList = converterListUsersToListUserProtected(usersService, userMapper);
		return userList;
	}

	public void printNameUserAll(Validators n, List<User> l) {
		if (n.equals(Validators.neutral)) {
			for (User u : l) {
				System.out.println(u.getName());
			}
		} else if (n.equals(Validators.affermative)) {
			for (User u : l) {
				System.out.println(u.toString());
			}
		}
	}

	/**
	 * 
	 * @param usersService
	 * @param userMapper
	 * @return
	 * 
	 */
	private List<User> converterListUsersToListUserNotProtected(UsersService usersService, UserMapper userMapper) {

		List<Users> UsersListFromDataUsers = usersService.getAllUsersFromDataBase();
		if (!UsersListFromDataUsers.isEmpty()) {
			List<User> userList = userMapper.getAllUsersInUserFormat(UsersListFromDataUsers);

			return userList;
		}

		return null;
	}

	/**
	 * 
	 * @return
	 * 
	 */
	private List<User> trasformListUsersIntoListUserComplite() {

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
		UserDto userdto = userMapper.toUserdtoFromUser(u.getId(), u.getName(), u.getPassword(), u.getAccessLevel());
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

//login user

	public User LoginAuthenticator(String name, String pw) {
		List<User> userList = new ArrayList<>();
		userList = trasformListUsersIntoListUserComplite();
		if (userList != null) {
			for (User u : userList) {
				if (u.getName().equals(name) && Hash.verify(pw, u.getPassword())) {
					return returnProtectedUser(u);

				}
			}
			System.out.println("nessun utente trovato come " + name + " o password errata");
			return null;
		} else {
			System.out.println("nessun utente nella lista");
			User u = new User(InvalidValues.secret.toString(), InvalidValues.secret.toString(),
					InvalidValues.secret.toString(), null);
			return u;
		}
	}

	private User returnProtectedUser(User u) {
		User user = new User(u.getId(), u.getName(), InvalidValues.secret.toString(), u.getAccessLevel());
		return user;
	}

	// change password
	public void changePassordToUser(String pw, String id) {
		usersService.changePw(id, pw);
	}

	public void changeAccessLevelToUser(int i, String id) {
		usersService.changeAl(id, i);
	}
	
	

}
