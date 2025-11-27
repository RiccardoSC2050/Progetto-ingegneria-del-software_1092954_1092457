package it.unibg.progetto.api.access_session;

import it.unibg.progetto.api.conditions.AccessLevel;

public final class ManagerSession {

	private static volatile Session current;

	private ManagerSession() {
	}

	public static void login(String userId, String name, AccessLevel level) {
		if (!isLoggedIn()) { // First first control (out of lock)
			synchronized (ManagerSession.class) { // Block synchronize
				if (!isLoggedIn()) { // Second control in lock
					current = new Session(userId, name, level.getLevel());
				}
			}
		}
	}

	public static void logout() {
		current = null;
	}

	private static boolean isLoggedIn() {
		return current != null;
	}

	public static Session getCurrent() {
		return current;
	}

}
