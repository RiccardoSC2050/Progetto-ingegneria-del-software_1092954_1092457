package it.unibg.progetto.api.application;

import it.unibg.progetto.api.access_session.ManageSession;
import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.operators.User;

public class Master {


	public Master() {
	}

	public static Master getIstance() {
		return new Master();
	}

	/**
	 * 
	 * @param name
	 * @param password
	 */
	public boolean login(String name, String password) {
		
		
			User u = ActionOnUseRS.getInstance().LoginAuthenticator(name, password);
			if(u!=null) {

		ManageSession.login(u.getId(), u.getAccessLevel());
		return true;
			}
			else
				return false;
	}

	/**
	 * 
	 */
	public void logout() {
		ManageSession.logout();
	}

}
