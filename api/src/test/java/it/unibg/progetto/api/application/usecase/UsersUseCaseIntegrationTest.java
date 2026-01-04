package it.unibg.progetto.api.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.app.ApiMain;
import it.unibg.progetto.api.application.dto.RootDto;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.InvalidValues;
import it.unibg.progetto.api.security.Hash;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.data.UsersRepository;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiMain.class)
class UsersUseCaseIntegrationTest {

	@Autowired
	private UsersUseCase usersUseCase;

	@Autowired
	private UsersRepository usersRepository;

	@BeforeEach
	@AfterEach
	void cleanUsersTable() {
		usersRepository.deleteAll();
	}

	@Test
	void addUserOnData_persistsUser() {
		String id = "ID_" + System.currentTimeMillis();
		String name = ("u_" + System.currentTimeMillis()).toLowerCase();
		String hashedPw = Hash.hash("pw12345678");

		User user = new User(id, name, hashedPw, AccessLevel.AL2);

		usersUseCase.addUserOnData(user);

		Users fromDb = usersRepository.findById(id).orElse(null);
		assertNotNull(fromDb);
		assertEquals(id, fromDb.getUuid());
		assertEquals(name, fromDb.getName());
		assertEquals(hashedPw, fromDb.getPassword());
		assertEquals(AccessLevel.AL2.getLevel(), fromDb.getAccessLevel());
	}

	@Test
	void deleteUser_removesUser() {
		String id = "ID_" + System.currentTimeMillis();
		String name = ("u_" + System.currentTimeMillis()).toLowerCase();
		String hashedPw = Hash.hash("pw12345678");

		User user = new User(id, name, hashedPw, AccessLevel.AL1);
		usersUseCase.addUserOnData(user);

		assertTrue(usersRepository.findById(id).isPresent());

		usersUseCase.deleteUser(user);

		assertFalse(usersRepository.findById(id).isPresent());
	}

	@Test
	void changePassordToUser_updatesPasswordInDb() {
		String id = "ID_" + System.currentTimeMillis();
		String name = ("u_" + System.currentTimeMillis()).toLowerCase();

		Users entity = new Users(id, name, "oldPw", AccessLevel.AL1.getLevel());
		usersRepository.save(entity);

		usersUseCase.changePassordToUser("newPw", id);

		Users updated = usersRepository.findById(id).orElse(null);
		assertNotNull(updated);
		assertEquals("newPw", updated.getPassword());
	}

	@Test
	void changeAccessLevelToUser_updatesAccessLevelInDb() {
		String id = "ID_" + System.currentTimeMillis();
		String name = ("u_" + System.currentTimeMillis()).toLowerCase();

		Users entity = new Users(id, name, "pw", 1);
		usersRepository.save(entity);

		usersUseCase.changeAccessLevelToUser(3, id);

		Users updated = usersRepository.findById(id).orElse(null);
		assertNotNull(updated);
		assertEquals(3, updated.getAccessLevel());
	}

	@Test
	void LoginAuthenticator_returnsProtectedUser_whenCredentialsOk() {
		String id = "ID_" + System.currentTimeMillis();
		String name = ("u_" + System.currentTimeMillis()).toLowerCase();
		String plainPw = "pw12345678";
		String hashedPw = Hash.hash(plainPw);

		// inserisco direttamente entity nel DB
		usersRepository.save(new Users(id, name, hashedPw, AccessLevel.AL2.getLevel()));

		User logged = usersUseCase.LoginAuthenticator(name, plainPw);

		assertNotNull(logged);
		assertEquals(id, logged.getId());
		assertEquals(name, logged.getName());
		assertEquals(AccessLevel.AL2.getLevel(), logged.getAccessLevelValue());

		// password mascherata
		assertEquals(InvalidValues.secret.toString(), logged.getPassword());
	}

	@Test
	void LoginAuthenticator_returnsNull_whenWrongPassword() {
		String id = "ID_" + System.currentTimeMillis();
		String name = ("u_" + System.currentTimeMillis()).toLowerCase();

		usersRepository.save(new Users(id, name, Hash.hash("pw12345678"), AccessLevel.AL1.getLevel()));

		assertNull(usersUseCase.LoginAuthenticator(name, "wrongPW"));
	}

	@Test
	void numberOfAllOperators_countsRowsInDb() {
		usersRepository.save(new Users("id1", "u1", "pw", 1));
		usersRepository.save(new Users("id2", "u2", "pw", 2));

		int count = usersUseCase.numberOfAllOperators();
		assertEquals(2, count);
	}

	@Test
	void trasformListUsersIntoListUserWithoutPassword_returnsUsersAndHidesPasswords() {
		usersRepository.save(new Users("id1", "u1", "pw1", 1));
		usersRepository.save(new Users("id2", "u2", "pw2", 2));

		List<User> list = usersUseCase.trasformListUsersIntoListUserWithoutPassword();

		assertNotNull(list);
		assertEquals(2, list.size());
	}
}
