package it.unibg.progetto.api.cli;
import it.unibg.progetto.api.application.usecase.UsersUseCase;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.Validators;
import it.unibg.progetto.api.security.session.SessionManager;
import it.unibg.progetto.api.domain.rules.InvalidValues;

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
	public Validators login(String name, String password) {

		User u = UsersUseCase.getInstance().LoginAuthenticator(name, password);
		if (u != null) {

			if (u.getId().equals(InvalidValues.secret.toString()) && u.getName().equals(InvalidValues.secret.toString())
					&& u.getPassword().equals(InvalidValues.secret.toString())) {
				return Validators.neutral;
			}

			SessionManager.login(u.getId(), u.getName(), u.getAccessLevel());
			return Validators.affermative;
		}
		return Validators.negative;

	}

	/**
	 * 
	 */
	public void logout() {
		SessionManager.logout();
	}

}
