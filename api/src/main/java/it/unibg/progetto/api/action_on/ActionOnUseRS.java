package it.unibg.progetto.api.action_on;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.atn.SemanticContext.Operator;
import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.HikariCheckpointRestoreLifecycle;
import org.springframework.stereotype.Component;

import ch.qos.logback.core.net.LoginAuthenticator;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.StrangeValues;
import it.unibg.progetto.api.dto.Rootdto;
import it.unibg.progetto.api.dto.Userdto;
import it.unibg.progetto.api.mapper.RootMapper;
import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.api.operators.User;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.hashcode.Hash;
import it.unibg.progetto.service.UsersService;

@Component
public class ActionOnUseRS {

	private static ActionOnUseRS instance;
	private final UserMapper userMapper;
	private final UsersService usersService;
	private final RootMapper rootMapper;

	@Autowired
	public ActionOnUseRS(UserMapper userMapper, UsersService usersService, RootMapper rootMapper) {

		this.userMapper = userMapper;
		this.usersService = usersService;
		this.rootMapper = rootMapper;
		instance = this;
	}

	public static ActionOnUseRS getInstance() {
		return instance;
	}

	// ROOT

	public int numberOfAllOperators() {
		List<User> userList = trasformListUsersIntoListUserComplite();
		if (userList == null)
			return 0;
		int i = 0;
		for (User u : userList) {

			i++;
		}
		return i;

	}

	public Rootdto rootIsOnData() {
		List<User> userList = trasformListUsersIntoListUserComplite();
		if (userList != null) {
			for (User u : userList) {
				if (u.getId().equals(String.valueOf(StrangeValues.ROOTid.getLevel()))
						// confronto case-insensitive, coerente con come salvi il root
						&& u.getName().equalsIgnoreCase(StrangeValues.ROOT.toString())
						&& u.getAccessLevelValue() == AccessLevel.AL5.getLevel()) {

					return rootMapper.fromUser(u);
				}
			}
		}
		return null;
	}

	public void addRootOnData(Root root) {
		Rootdto rootdto = rootMapper.toRootdtoFromRoot(root.getId(), root.getName(), root.getPassword(),
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
			User u = new User(StrangeValues.secret.toString(), StrangeValues.secret.toString(),
					StrangeValues.secret.toString(), null);
			return u;
		}
	}

	private User returnProtectedUser(User u) {
		User user = new User(u.getId(), u.getName(), StrangeValues.secret.toString(), u.getAccessLevel());
		return user;
	}

}
