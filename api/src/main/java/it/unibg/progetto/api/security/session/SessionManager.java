package it.unibg.progetto.api.security.session;
import it.unibg.progetto.api.domain.rules.AccessLevel;

public final class SessionManager {

	private static volatile Session current;

	private SessionManager() {
	}

	public static void login(String userId, String name, AccessLevel level) {
		if (!isLoggedIn()) { // First first control (out of lock)
			synchronized (SessionManager.class) { // Block synchronize
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
