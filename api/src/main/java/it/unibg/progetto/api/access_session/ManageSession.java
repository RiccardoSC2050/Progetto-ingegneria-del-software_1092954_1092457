package it.unibg.progetto.api.access_session;

import it.unibg.progetto.api.operators.AccessLevel;

public final class ManageSession {

	private static volatile Session current;

	private ManageSession() {
	}

	public static void login(String userId, AccessLevel level) {
		if (!isLoggedIn()) { 						// First first control (out of lock)
			synchronized (ManageSession.class) { 	// Block synchronize
				if (!isLoggedIn()) { 				// Second control in lock
					current = new Session(userId, level.getLevel());
				}
			}
		}
	}

	public static void logout() {
		current = null;
	}

	public static boolean isLoggedIn() {
		return current != null;
	}

	public static Session getCurrent() {
		return current;
	}

}
