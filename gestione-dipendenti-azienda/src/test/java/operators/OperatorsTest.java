package operators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * sottoclassi di operators
 */
public class OperatorsTest {

	/**
	 * UserTest
	 * 
	 * @throws InvalidAccessLevelException
	 */
	@Test // correct
	void testCostructorOperatorUser() throws InvalidAccessLevelException {
		User testUser = new User("user", "pw", 1);

		assertEquals("user", testUser.getName());
		assertEquals("pw", testUser.getPassword());
		assertEquals(1, testUser.getAccessLevel());
		assertNotNull(testUser.getId());

		User testUser2 = new User("user2", "pw", 2);
		assertEquals("user2", testUser2.getName());
		assertEquals("pw", testUser2.getPassword());
		assertEquals(2, testUser2.getAccessLevel());
		assertNotNull(testUser2.getId());

		// check the id are the same
		assertNotEquals(testUser.getId(), testUser2.getId());
	}

	/**
	 * RootTest
	 */
	@Test // correct
	void testCostructorOperatorRoot() {

		Root root1 = Root.getInstanceRoot();
		Root root2 = Root.getInstanceRoot();

		assertNotNull(root1);
		assertNotNull(root2);
		assertEquals(root1, root2);

	}

	/**
	 * Operator Method
	 * 
	 * @throws InvalidAccessLevelException
	 */
	@Test
	void operatorMethodTest() throws InvalidAccessLevelException {

		User user = new User("user", "pw", 2);
		Root root = Root.getInstanceRoot();

		// public boolean hasAtLeast(int lv, AccessLevel level)
		assertTrue(user.hasAtLeast(1, AccessLevel.AL1));
		assertTrue(user.hasAtLeast(2, AccessLevel.AL2));
		assertTrue(user.hasAtLeast(3, AccessLevel.AL3));

		// public boolean login(String name, String password)
		assertFalse(user.isFlagLogin());
		assertFalse(root.isFlagLogin());
		
		
		//user
		user.login("Root", "pw");
		assertFalse(user.isFlagLogin());
		user.login("User", "1234");
		assertFalse(user.isFlagLogin());
		user.login("Root", "1234");
		assertFalse(user.isFlagLogin());
		user.login("user", "pw");
		assertTrue(user.isFlagLogin());
		
		
		//root
		root.login("ROOT", "pw");
		assertFalse(root.isFlagLogin());
		root.login("User", "1234");
		assertFalse(root.isFlagLogin());
		root.login("user", "pw");
		assertFalse(root.isFlagLogin());
		root.login("ROOT", "1234");
		assertTrue(root.isFlagLogin());

		// public void logout()
		
		//user
		user.logout();
		assertFalse(user.isFlagLogin());
		//root
		root.logout();
		assertFalse(root.isFlagLogin());
		
		// public static void deleteSpecificOperatorFromAllOperators(Operator o)
		

	}

	
}
