package it.unibg.progetto.api.security.session;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class SessionTest {

	@Test
	void constructor_setsFieldsCorrectly() {
		Session s = new Session("uuid-1", "mario", 2);

		assertEquals("uuid-1", s.getUuid());
		assertEquals("mario", s.getName());
		assertEquals(2, s.getAccessLevel());
	}

	@Test
	void loginTime_isNotNull_andIsCloseToNow() {
		Instant before = Instant.now();
		Session s = new Session("uuid-1", "mario", 2);
		Instant after = Instant.now();

		assertNotNull(s.getLoginTime());

		
		assertFalse(s.getLoginTime().isBefore(before));
		assertFalse(s.getLoginTime().isAfter(after));
	}

	@Test
	void loginTime_isFixedForSameSessionInstance() throws Exception {
		Session s = new Session("uuid-1", "mario", 2);

		Instant t1 = s.getLoginTime();
		Thread.sleep(5);
		Instant t2 = s.getLoginTime();

		
		assertEquals(t1, t2);
	}

	@Test
	void differentSessions_haveDifferentLoginTimes_orNonDecreasing() throws Exception {
		Session s1 = new Session("uuid-1", "mario", 2);
		Thread.sleep(2);
		Session s2 = new Session("uuid-2", "anna", 3);

		
		assertFalse(s2.getLoginTime().isBefore(s1.getLoginTime()));
	}
}
