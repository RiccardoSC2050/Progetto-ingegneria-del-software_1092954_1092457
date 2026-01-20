package it.unibg.progetto.api.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.application.usecase.UsersUseCase;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.Validators;
import it.unibg.progetto.api.security.session.SessionManager;

class MasterTest {

	@Test
	void login_whenAuthenticatorReturnsNull_returnsNegative_andDoesNotLoginSession() {
		Master sut = new Master();

		UsersUseCase usersUseCase = mock(UsersUseCase.class);

		try (MockedStatic<UsersUseCase> uc = mockStatic(UsersUseCase.class);
				MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {

			uc.when(UsersUseCase::getInstance).thenReturn(usersUseCase);
			when(usersUseCase.LoginAuthenticator("mario", "pw")).thenReturn(null);

			Validators result = sut.login("mario", "pw");

			assertEquals(Validators.negative, result);
			sm.verifyNoInteractions(); // non deve chiamare SessionManager.login/logout
		}
	}

	@Test
	void login_whenSecretUser_returnsNeutral_andDoesNotLoginSession() {
		Master sut = new Master();

		UsersUseCase usersUseCase = mock(UsersUseCase.class);

		// Utente "secret" (id,name,password = "secret") => branch neutral
		User secret = mock(User.class);
		when(secret.getId()).thenReturn("secret");
		when(secret.getName()).thenReturn("secret");
		when(secret.getPassword()).thenReturn("secret");

		try (MockedStatic<UsersUseCase> uc = mockStatic(UsersUseCase.class);
				MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {

			uc.when(UsersUseCase::getInstance).thenReturn(usersUseCase);
			when(usersUseCase.LoginAuthenticator("x", "y")).thenReturn(secret);

			Validators result = sut.login("x", "y");

			assertEquals(Validators.neutral, result);
			sm.verifyNoInteractions(); // NON deve fare SessionManager.login
		}
	}

	@Test
	void login_whenValidUser_returnsAffermative_andCallsSessionManagerLogin() {
		Master sut = new Master();

		UsersUseCase usersUseCase = mock(UsersUseCase.class);

		// Utente normale
		User u = mock(User.class);
		when(u.getId()).thenReturn("U123");
		when(u.getName()).thenReturn("luca");
		when(u.getAccessLevel()).thenReturn(AccessLevel.AL2);

		// importante: NON deve risultare "secret" su tutte e 3
		when(u.getPassword()).thenReturn("hashQualsiasi");

		try (MockedStatic<UsersUseCase> uc = mockStatic(UsersUseCase.class);
				MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {

			uc.when(UsersUseCase::getInstance).thenReturn(usersUseCase);
			when(usersUseCase.LoginAuthenticator("luca", "pw")).thenReturn(u);

			Validators result = sut.login("luca", "pw");

			assertEquals(Validators.affermative, result);

			sm.verify(() -> SessionManager.login("U123", "luca", AccessLevel.AL2));
			sm.verifyNoMoreInteractions();
		}
	}

	@Test
	void logout_callsSessionManagerLogout() {
		Master sut = new Master();

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {
			sut.logout();
			sm.verify(SessionManager::logout);
		}
	}
}
