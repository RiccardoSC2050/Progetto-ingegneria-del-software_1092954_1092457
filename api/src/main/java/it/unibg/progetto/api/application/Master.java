package it.unibg.progetto.api.application;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.conditions.Checks;
import it.unibg.progetto.api.conditions.StrangeValues;
import it.unibg.progetto.api.operators.User;

public class Master {

	public Master() {
	}

	public static Master getIstance() {
		return new Master();
	}

	public void RootInData() {

	}

	// access
	/**
	 * 
	 * @param name
	 * @param password
	 */
	public Checks login(String name, String password) {

		User u = ActionOnUseRS.getInstance().LoginAuthenticator(name, password);
		if (u != null) {

			if (u.getId().equals(StrangeValues.secret.toString()) && u.getName().equals(StrangeValues.secret.toString())
					&& u.getPassword().equals(StrangeValues.secret.toString())) {
				return Checks.neutral;
			}

			ManagerSession.login(u.getId(), u.getName(), u.getAccessLevel());
			return Checks.affermative;
		}
		return Checks.negative;

	}

	/**
	 * 
	 */
	public void logout() {
		ManagerSession.logout();
	}

}
