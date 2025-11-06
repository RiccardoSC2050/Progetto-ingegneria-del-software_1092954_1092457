package operators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class UsersTest {

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
}
