package it.unibg.progetto.api.cli;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.app.ApiMain;
import it.unibg.progetto.api.application.dto.RootDto;
import it.unibg.progetto.api.application.usecase.UsersUseCase;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.InvalidValues;
import it.unibg.progetto.api.security.Hash;
import it.unibg.progetto.data.UsersRepository;

@SpringBootTest(classes = ApiMain.class)
@ActiveProfiles("test") // usa api/src/test/resources/application-test.properties
class AppBlocksManageUsersIntegrationTes {

	@Autowired
	private UsersUseCase usersUseCase;

	@Autowired
	private UsersRepository usersRepository;

	@BeforeEach
	void cleanDb() {
		// IMPORTANTISSIMO: ogni test parte pulito
		usersRepository.deleteAll();
	}

	@Test
	void addRootOnData_then_rootIsOnData_returnsRootDto() {
		// Arrange: creo "root" come record Users (via usecase)
		var root = new it.unibg.progetto.api.domain.Root(Hash.hash("passwordRoot123"));

		// Act
		usersUseCase.addRootOnData(root);
		RootDto saved = usersUseCase.rootIsOnData();

		// Assert
		assertNotNull(saved, "RootDto deve esistere dopo addRootOnData()");
		assertEquals(String.valueOf(InvalidValues.ROOTid.getLevel()), saved.getUuid());
		assertEquals(
			    InvalidValues.ROOT.toString().toLowerCase(),
			    saved.getUsername().toLowerCase()
			);

	}

	@Test
	void addUserOnData_then_numberOfAllOperators_is1() {
		// Arrange
		String hashed = Hash.hash("pwUtente123");
		User u = new User("mario", hashed, AccessLevel.AL1);

		// Act
		usersUseCase.addUserOnData(u);

		// Assert
		assertEquals(1, usersUseCase.numberOfAllOperators());
	}

	@Test
	void deleteUser_removesUserFromDb() {
		// Arrange
		User u = new User("luca", Hash.hash("pwLuca123"), AccessLevel.AL2);
		usersUseCase.addUserOnData(u);
		assertEquals(1, usersUseCase.numberOfAllOperators());

		// Act
		usersUseCase.deleteUser(u);

		// Assert
		assertEquals(0, usersUseCase.numberOfAllOperators());
	}

	@Test
	void loginAuthenticator_withCorrectPassword_returnsProtectedUser() {
		// Arrange
		String clearPw = "pwSegreta123";
		String hashed = Hash.hash(clearPw);

		User u = new User("anna", hashed, AccessLevel.AL3);
		usersUseCase.addUserOnData(u);

		// Act
		User logged = usersUseCase.LoginAuthenticator("anna", clearPw);

		// Assert
		assertNotNull(logged);
		assertEquals("anna", logged.getName());
		// la password deve essere "protetta"/mascherata (non l'hash vero)
		assertNotEquals(hashed, logged.getPassword());
		assertNotNull(logged.getAccessLevel());
	}

	@Test
	void loginAuthenticator_wrongPassword_returnsNull() {
		// Arrange
		User u = new User("paolo", Hash.hash("pwPaolo123"), AccessLevel.AL1);
		usersUseCase.addUserOnData(u);

		// Act
		User logged = usersUseCase.LoginAuthenticator("paolo", "passwordSbagliata");

		// Assert
		assertNull(logged);
	}
}
