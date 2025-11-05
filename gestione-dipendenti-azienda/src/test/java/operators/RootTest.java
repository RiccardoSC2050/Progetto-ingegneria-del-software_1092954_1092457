package operators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RootTest {
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
	
	@Test
	void TestcreateUser() throws InvalidAccessLevelException {
		
		Root root = Root.getInstanceRoot();
		
		root.createUser("pippo", "pallino", 2);
		
		assertFalse(Operator.getAllUsers().isEmpty());
		
	System.out.println("--------------------------------------------");	
	System.out.println("Verifica testuale TEST CREATE USER");	
	System.out.println(Operator.getAllOperators()+ "questo è operators");
	System.out.println(Operator.getAllUsers()+ "questo è users");
		
	}
	
	@Test
	void TestdeleteUser() throws InvalidAccessLevelException {
		
		Root root = Root.getInstanceRoot();
		
		root.createUser("n", "pw", 1);
		
		root.deleteUser();
		
		
		assertTrue(Operator.getAllUsers().isEmpty());
		
		System.out.println("--------------------------------------------");	
		System.out.println("Verifica testuale TEST DELETE USER");	
		System.out.println(Operator.getAllOperators()+ "questo è operators");
		System.out.println(Operator.getAllUsers()+ "questo è users");
		
	}
	
	
}
