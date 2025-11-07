package it.unibg.progetto.api.operators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.api.operators.InvalidAccessLevelException;
import it.unibg.progetto.api.operators.Operator;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.service.UsersService;

public class RootTest {
	/**
	 * RootTest
	 */
	@Test // correct
	void testCostructorOperatorRoot(UsersService service, UserMapper userMapper) {

		Root root1 = Root.getInstanceRoot(userMapper, service);
		Root root2 = Root.getInstanceRoot(userMapper, service);

		assertNotNull(root1);
		assertNotNull(root2);
		assertEquals(root1, root2);

	}
	
	@Test
	void TestcreateUser() throws InvalidAccessLevelException {
		
		Root root = Root.getInstanceRoot(null, null);
		
		root.createUser("pippo", "pallino", 2);
		
		assertFalse(Operator.getAllUsers().isEmpty());
		
	System.out.println("--------------------------------------------");	
	System.out.println("Verifica testuale TEST CREATE USER");	
	System.out.println(Operator.getAllOperators()+ "questo è operators");
	System.out.println(Operator.getAllUsers()+ "questo è users");
		
	}
	
	@Test
	void TestdeleteUser(UsersService service, UserMapper userMapper) throws InvalidAccessLevelException {
		
		Root root = Root.getInstanceRoot(userMapper, service);
		
		root.createUser("n", "pw", 1);
		
		root.deleteUser();
		
		
		assertTrue(Operator.getAllUsers().isEmpty());
		
		System.out.println("--------------------------------------------");	
		System.out.println("Verifica testuale TEST DELETE USER");	
		System.out.println(Operator.getAllOperators()+ "questo è operators");
		System.out.println(Operator.getAllUsers()+ "questo è users");
		
	}
	
	
}
