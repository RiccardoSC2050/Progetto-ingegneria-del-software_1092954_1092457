package it.unibg.progetto.api.operators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.api.operators.AccessLevel;
import it.unibg.progetto.api.operators.InvalidAccessLevelException;
import it.unibg.progetto.api.operators.Operator;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.api.operators.User;
import it.unibg.progetto.service.UsersService;

/**
 * sottoclassi di operators
 */
/*
public class OperatorsTest {

	/**
	 * Operator Method
	 * 
	 * @throws InvalidAccessLevelException
	 */
	/*
	@Test
	void operatorMethodTest(UsersService service, UserMapper userMapper) throws InvalidAccessLevelException {

		User user = new User("user", "pw", 2);
		Root root = Root.getInstanceRoot(userMapper, service);

		// public boolean hasAtLeast(int lv, AccessLevel level)
		assertTrue(user.hasAtLeast(1, AccessLevel.AL1));
		assertTrue(user.hasAtLeast(2, AccessLevel.AL2));
		assertTrue(user.hasAtLeast(3, AccessLevel.AL3));

		// public boolean login(String name, String password)
		assertFalse(user.isFlagLogin());
		assertFalse(root.isFlagLogin());

		// user
		user.login("Root", "pw");
		assertFalse(user.isFlagLogin());
		user.login("User", "1234");
		assertFalse(user.isFlagLogin());
		user.login("Root", "1234");
		assertFalse(user.isFlagLogin());
		user.login("user", "pw");
		assertTrue(user.isFlagLogin());

		// root
		root.login("ROOT", "pw");
		assertFalse(root.isFlagLogin());
		root.login("User", "1234");
		assertFalse(root.isFlagLogin());
		root.login("user", "pw");
		assertFalse(root.isFlagLogin());
		root.login("ROOT", "1234");
		assertTrue(root.isFlagLogin());

		// public void logout()

		// user
		user.logout();
		assertFalse(user.isFlagLogin());
		// root
		root.logout();
		assertFalse(root.isFlagLogin());

	}

	// public static void deleteSpecificOperatorFromAllOperators(Operator o)

	@Test
	void testdeleteSpecificOperatorFromAllOperators() throws InvalidAccessLevelException {
		User u1 = new User("r", "p", 1);
		User u2 = new User("r", "p", 1);
		User u3 = new User("r", "p", 1);

		// remove u1
		Operator.deleteSpecificOperatorFromAllOperators(u1);

		assertFalse(Operator.getAllOperators().contains(u1));

	}

}*/
