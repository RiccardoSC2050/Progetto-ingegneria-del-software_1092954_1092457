package it.unibg.progetto.api.security.session;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.domain.rules.AccessLevel;

class SessionManagerTest {

	@AfterEach
	void cleanup() {
		SessionManager.logout(); 
	}

	@Test
	void initially_currentIsNull() {
		assertNull(SessionManager.getCurrent());
	}

	@Test
	void login_setsCurrentSession_onlyOnce() {
		SessionManager.login("uuid-1", "mario", AccessLevel.AL2);

		Session current = SessionManager.getCurrent();
		assertNotNull(current);
		assertEquals("uuid-1", current.getUuid());
		assertEquals("mario", current.getName());
		assertEquals(AccessLevel.AL2.getLevel(), current.getAccessLevel());
	}

	@Test
	void login_whenAlreadyLogged_doesNotOverwriteCurrent() {
		SessionManager.login("uuid-1", "mario", AccessLevel.AL1);
		Session first = SessionManager.getCurrent();

		
		SessionManager.login("uuid-2", "anna", AccessLevel.AL3);
		Session after = SessionManager.getCurrent();

		assertSame(first, after);
		assertEquals("uuid-1", after.getUuid());
		assertEquals("mario", after.getName());
		assertEquals(AccessLevel.AL1.getLevel(), after.getAccessLevel());
	}

	@Test
	void logout_clearsCurrent() {
		SessionManager.login("uuid-1", "mario", AccessLevel.AL2);
		assertNotNull(SessionManager.getCurrent());

		SessionManager.logout();
		assertNull(SessionManager.getCurrent());
	}

	@Test
	void accessLevelOption_printsMessageForAL1() {
		SessionManager.login("uuid-1", "mario", AccessLevel.AL1);

		String out = captureStdout(() -> SessionManager.accessLevelOption());

		assertTrue(out.contains("LIVELLO DI ACCESSO 1"));
	}

	@Test
	void accessLevelOption_printsMessageForAL2() {
		SessionManager.login("uuid-1", "mario", AccessLevel.AL2);

		String out = captureStdout(() -> SessionManager.accessLevelOption());

		assertTrue(out.contains("LIVELLO DI ACCESSO 2"));
	}

	@Test
	void accessLevelOption_printsMessageForAL3() {
		SessionManager.login("uuid-1", "mario", AccessLevel.AL3);

		String out = captureStdout(() -> SessionManager.accessLevelOption());

		assertTrue(out.contains("LIVELLO DI ACCESSO 3"));
	}

	@Test
	void accessLevelOption_printsMessageForAL5() {
		SessionManager.login("uuid-1", "mario", AccessLevel.AL5);

		String out = captureStdout(() -> SessionManager.accessLevelOption());

		assertTrue(out.contains("LIVELLO DI ACCESSO ROOT"));
	}

	
	private static String captureStdout(Runnable action) {
		PrintStream old = System.out;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos, true));

		try {
			action.run();
		} finally {
			System.setOut(old);
		}

		return new String(baos.toByteArray(), StandardCharsets.UTF_8);
	}
}
