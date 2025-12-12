package it.unibg.progetto.api.operators;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.application.ApiMain;
import it.unibg.progetto.api.components.GlobalScaner;

@SpringBootTest(classes = ApiMain.class)
@ActiveProfiles("test")
class RootTest {

	private Root root;

	@BeforeEach
	void setUp() {
		// create object for each test

		root = Root.getInstanceRoot();

	}

	// ---------- 1. Costruttore e singleton ----------

	@Test
	void getInstanceRootReturnsSameInstance() {
		Root r1 = Root.getInstanceRoot();
		Root r2 = Root.getInstanceRoot();

		// Deve essere la stessa istanza (pattern Singleton)
		assertSame(r1, r2, "getInstanceRoot() deve restituire sempre la stessa istanza");
	}

	@Test
	void getInstanceRootInitializesRootWithFixedValues() {

		// Nome fissato per Root dal costruttore di Operator
		assertEquals("ROOT", root.getName());

		// Id e livello di accesso fissi per l'amministratore
		assertEquals("0", root.getId());
		assertEquals(5, root.getAccessLevelValue());

		// AccessLevel "oggetto" non viene impostato esplicitamente
		assertNull(root.getAccessLevel(), "AccessLevel non viene impostato esplicitamente per Root");

		// La password ora arriva dal database: verifichiamo solo che esista
		assertNotNull(root.getPassword());
		assertFalse(root.getPassword().isBlank());
	}

	@Test
	void manualRootConstructorSetsIdZeroAndLevelFive() {
		Root customRoot = new Root("pwd");

		// Nome e password presi dal costruttore
		assertEquals("ROOT", customRoot.getName());
		assertEquals("pwd", customRoot.getPassword());

		// Id e livello di accesso fissati per Root
		assertEquals("0", customRoot.getId());
		assertEquals(5, customRoot.getAccessLevelValue());
	}

	// ------------------method create user --------------------
	@Test
	void isPossibleTocreateUserTest() {

		String fakeInput = String.join("\n", "tester", // name
				"secret", // password
				"2" // level
		) + "\n";

		ByteArrayInputStream in = new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8));

		// finto stdin
		System.setIn(in);

		GlobalScaner.scanner = new Scanner(System.in);

		root.createUser();
	}

	// ------------------method delete user --------------------
	@Test
	void delUserTest() {
		String id = null;
		List<User> userList = ActionOnUseRS.getInstance().trasformListUsersIntoListUserWithoutPassword();
		if (userList != null) {
			for (User u : userList) {
				if (u.getName().toLowerCase().equals("tester")) {
					id = u.getId();
				}
			}

			String fakeInput = String.join("\n", "tester", // name
					"y", // password
					"y", // level
					id // level
			) + "\n";

			ByteArrayInputStream in = new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8));

			// finto stdin
			System.setIn(in);

			GlobalScaner.scanner = new Scanner(System.in);
		}
		root.deleteUser();
		root.deleteUser();
	}

}
