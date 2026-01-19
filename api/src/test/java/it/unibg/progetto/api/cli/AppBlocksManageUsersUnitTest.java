package it.unibg.progetto.api.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.application.usecase.UsersUseCase;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.cli.components.Quit;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.InvalidValues;
import it.unibg.progetto.api.domain.rules.Validators;

import it.unibg.progetto.api.security.session.Session;
import it.unibg.progetto.api.security.session.SessionManager;

class AppBlocksManageUsersUnitTest {

	@AfterEach
	void resetScanner() {
		GlobalScanner.scanner = new Scanner(System.in);
	}

	@Test
	void createUserIfRoot_callsRootCreateUser_whenAccessIsAL5() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL5.getLevel());

		Root root = mock(Root.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<Root> rt = mockStatic(Root.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			rt.when(Root::getInstanceRoot).thenReturn(root);

			sut.createUserIfRoot();

			verify(root).createUser();
		}
	}

	@Test
	void createUserIfRoot_doesNothing_whenNotRoot() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

		Root root = mock(Root.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<Root> rt = mockStatic(Root.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			rt.when(Root::getInstanceRoot).thenReturn(root);

			sut.createUserIfRoot();

			verify(root, never()).createUser();
		}
	}

	@Test
	void deleteUserIfRoot_callsRootDeleteUser_whenAccessIsAL5() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL5.getLevel());

		Root root = mock(Root.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<Root> rt = mockStatic(Root.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			rt.when(Root::getInstanceRoot).thenReturn(root);

			sut.deleteUserIfRoot();

			verify(root).deleteUser();
		}
	}

	@Test
	void deleteUserIfRoot_doesNothing_whenNotRoot() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL2.getLevel());

		Root root = mock(Root.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<Root> rt = mockStatic(Root.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			rt.when(Root::getInstanceRoot).thenReturn(root);

			sut.deleteUserIfRoot();

			verify(root, never()).deleteUser();
		}
	}

	@Test
	void showUsersIfRoot_callsUsersUseCase_whenAccessIsAL5() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL5.getLevel());

		UsersUseCase usersUseCase = mock(UsersUseCase.class);

		User u1 = new User("1", "u1", InvalidValues.secret.toString(), AccessLevel.AL1);
		when(usersUseCase.numberOfAllOperators()).thenReturn(2);
		when(usersUseCase.trasformListUsersIntoListUserWithoutPassword()).thenReturn(Arrays.asList(u1));

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<UsersUseCase> uu = mockStatic(UsersUseCase.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uu.when(UsersUseCase::getInstance).thenReturn(usersUseCase);

			sut.showUsersIfRoot(Validators.neutral);

			verify(usersUseCase).numberOfAllOperators();
			verify(usersUseCase).trasformListUsersIntoListUserWithoutPassword();
			verify(usersUseCase).printNameUserAll(eq(Validators.neutral), anyList());
		}
	}

	@Test
	void showUsersIfRoot_doesNothing_whenNotRoot() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

		UsersUseCase usersUseCase = mock(UsersUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<UsersUseCase> uu = mockStatic(UsersUseCase.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uu.when(UsersUseCase::getInstance).thenReturn(usersUseCase);

			sut.showUsersIfRoot(Validators.neutral);

			verifyNoInteractions(usersUseCase);
		}
	}

	@Test
	void viewOtherFiles_returnsImmediately_whenAccessBelowAL3() throws Exception {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL2.getLevel()); // < AL3

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {
			sm.when(SessionManager::getCurrent).thenReturn(session);

			assertDoesNotThrow(() -> sut.viewOtherFiles());
		}
	}

	@Test
	void changePassword_updatesPasswordInUseCase_whenValidPw() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("password99\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		when(session.getUuid()).thenReturn("ID1");

		Root root = mock(Root.class);
		when(root.checkLenghtPw("password99")).thenReturn(true);

		UsersUseCase usersUseCase = mock(UsersUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<Root> rt = mockStatic(Root.class);
				MockedStatic<UsersUseCase> uu = mockStatic(UsersUseCase.class);
				MockedStatic<Quit> qt = mockStatic(Quit.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			rt.when(Root::getInstanceRoot).thenReturn(root);
			uu.when(UsersUseCase::getInstance).thenReturn(usersUseCase);
			qt.when(() -> Quit.quit(anyString())).thenReturn(false);

			sut.changePassword();

			// catturo argomenti reali
			var pwCaptor = org.mockito.ArgumentCaptor.forClass(String.class);
			verify(usersUseCase).changePassordToUser(pwCaptor.capture(), eq("ID1"));

			String passedHash = pwCaptor.getValue();
			assertNotNull(passedHash);
			assertNotEquals("password99", passedHash); // non deve passare in chiaro

			// se è BCrypt (molto probabile), puoi anche usare questa:
			// assertTrue(passedHash.startsWith("$2a$") || passedHash.startsWith("$2b$") ||
			// passedHash.startsWith("$2y$"));
		}
	}

	@Test
	void changePassword_returns_whenQuit() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("q\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		when(session.getUuid()).thenReturn("ID1");

		UsersUseCase usersUseCase = mock(UsersUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<UsersUseCase> uu = mockStatic(UsersUseCase.class);
				MockedStatic<Quit> qt = mockStatic(Quit.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uu.when(UsersUseCase::getInstance).thenReturn(usersUseCase);

			qt.when(() -> Quit.quit("q")).thenReturn(true);

			sut.changePassword();

			verifyNoInteractions(usersUseCase);
		}
	}

	@Test
	void changeAccLev_returns_whenQuitOnFirstInput() {
		AppBlocksManageUsers sut = new AppBlocksManageUsers();

		// primo input: nome utente -> quit
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("q\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL5.getLevel());

		UsersUseCase usersUseCase = mock(UsersUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<UsersUseCase> uu = mockStatic(UsersUseCase.class);
				MockedStatic<Quit> qt = mockStatic(Quit.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uu.when(UsersUseCase::getInstance).thenReturn(usersUseCase);
			qt.when(() -> Quit.quit("q")).thenReturn(true);

			sut.changeAccLev();

			verify(usersUseCase, never()).changeAccessLevelToUser(anyInt(), anyString());
		}
	}
}
