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

	public static void accessLevelOption() {
		if(current.getAccessLevel()==AccessLevel.AL1.getLevel()) {
			System.out.println("[LIVELLO DI ACCESSO 1: SOLO LETTURA");
		}
		else if(current.getAccessLevel()==AccessLevel.AL2.getLevel()) {
			System.out.println("[LIVELLO DI ACCESSO 2: SALVATAGGIO E MODIFICA FILE PERSONALI");
		}
		else if(current.getAccessLevel()==AccessLevel.AL3.getLevel()) {
			System.out.println("[LIVELLO DI ACCESSO 3: SALVATAGGIO E MODIFICA DI FILE PERSONALI, LETTURA DI FILE ALTRUI");
		}
		else if(current.getAccessLevel()==AccessLevel.AL5.getLevel()) {
			System.out.println("[LIVELLO DI ACCESSO ROOT: CREAZIONE ED ELIMINAZIONE UTENTI, LETTURA DEI FILE E CANCELLAZIONE DI QUALSIASI");
		}
	}
}
