package it.unibg.progetto.api.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.application.dto.RootDto;
import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.application.usecase.UsersUseCase;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.security.Hash;

/**
 * Test unitario della classe Root. La logica viene verificata in isolamento
 * utilizzando Mockito per simulare UsersUseCase, senza coinvolgere il database.
 */
class RootTest {

	private InputStream originalIn;
	private Scanner originalScanner;

	private CsvUseCase originalCsvUseCaseInstance;

	/**
	 * Prima di ogni test: - azzero il singleton Root.root (test indipendenti) -
	 * salvo/ripristino System.in e GlobalScanner.scanner (test indipendenti) -
	 * salvo/ripristino CsvUseCase.istance (evito effetti collaterali)
	 */
	@BeforeEach
	void setup() throws Exception {
		// backup input/scanner globali
		originalIn = System.in;
		originalScanner = GlobalScanner.scanner;

		// reset singleton Root.root
		Field rootField = Root.class.getDeclaredField("root");
		rootField.setAccessible(true);
		rootField.set(null, null);

		// backup CsvUseCase.istance (static)
		Field csvField = CsvUseCase.class.getDeclaredField("istance");
		csvField.setAccessible(true);
		originalCsvUseCaseInstance = (CsvUseCase) csvField.get(null);
	}

	@AfterEach
	void teardown() throws Exception {
		// ripristino input
		System.setIn(originalIn);

		// chiudo lo scanner creato nel test (se diverso dall'originale)
		if (GlobalScanner.scanner != null && GlobalScanner.scanner != originalScanner) {
			try {
				GlobalScanner.scanner.close();
			} catch (Exception ignored) {
			}
		}
		GlobalScanner.scanner = originalScanner;

		// ripristino CsvUseCase.istance
		Field csvField = CsvUseCase.class.getDeclaredField("istance");
		csvField.setAccessible(true);
		csvField.set(null, originalCsvUseCaseInstance);
	}

	// Helper: setta input finto + scanner globale
	private void setFakeInput(String... lines) {
		String fakeInput = String.join("\n", lines) + "\n";
		System.setIn(new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8)));
		GlobalScanner.scanner = new Scanner(System.in);
	}

	// Helper: forza CsvUseCase.getIstnce() a non essere null (unit test senza
	// Spring)
	private void setCsvUseCaseMock(CsvUseCase csvMock) throws Exception {
		Field csvField = CsvUseCase.class.getDeclaredField("istance");
		csvField.setAccessible(true);
		csvField.set(null, csvMock);
	}

	// ---------- 1. Singleton: getInstanceRoot() ----------

	@Test
	void getInstanceRootReturnsSameInstance() {
		try (MockedStatic<UsersUseCase> mockedStatic = mockStatic(UsersUseCase.class)) {
			UsersUseCase mockService = mock(UsersUseCase.class);
			mockedStatic.when(UsersUseCase::getInstance).thenReturn(mockService);

			RootDto dto = new RootDto();
			dto.setPassword("pwd");
			when(mockService.rootIsOnData()).thenReturn(dto);

			Root r1 = Root.getInstanceRoot();
			Root r2 = Root.getInstanceRoot();

			assertSame(r1, r2, "getInstanceRoot() deve restituire sempre la stessa istanza");
		}
	}

	@Test
	void getInstanceRootInitializesRootWithFixedValues() {
		try (MockedStatic<UsersUseCase> mockedStatic = mockStatic(UsersUseCase.class)) {
			UsersUseCase mockService = mock(UsersUseCase.class);
			mockedStatic.when(UsersUseCase::getInstance).thenReturn(mockService);

			RootDto dto = new RootDto();
			dto.setPassword("pwd");
			when(mockService.rootIsOnData()).thenReturn(dto);

			Root root = Root.getInstanceRoot();

			assertEquals("ROOT", root.getName(), "Il nome di Root deve essere 'ROOT'");
			assertEquals("0", root.getId(), "L'id di Root deve essere '0'");
			assertEquals(5, root.getAccessLevelValue(), "Il livello di accesso di Root deve essere 5");
			assertNull(root.getAccessLevel(), "AccessLevel non viene impostato esplicitamente per Root");
			assertEquals("pwd", root.getPassword(), "La password deve essere quella letta dal DTO");
		}
	}

	// ---------- 2. Costruttore manuale ----------

	@Test
	void manualRootConstructorSetsIdZeroAndLevelFive() {
		Root customRoot = new Root("pwd");

		assertEquals("ROOT", customRoot.getName(), "Il nome di Root deve essere 'ROOT'");
		assertEquals("pwd", customRoot.getPassword(), "La password passata al costruttore deve essere mantenuta");
		assertEquals("0", customRoot.getId(), "L'id di Root deve essere '0'");
		assertEquals(5, customRoot.getAccessLevelValue(), "Il livello di accesso di Root deve essere 5");
	}

	// ---------- 3. createUser() (unit test con mock UseCase) ----------

	@Test
	void createUserCallsAddUserOnDataOnValidInput() {
		Root root = new Root("pwd");

		// password >= 8 (altrimenti il metodo ripete la domanda e finisci l'input)
		setFakeInput("tester", "secret123", "2");

		try (MockedStatic<UsersUseCase> mockedStatic = mockStatic(UsersUseCase.class)) {
			UsersUseCase mockService = mock(UsersUseCase.class);
			mockedStatic.when(UsersUseCase::getInstance).thenReturn(mockService);

			when(mockService.trasformListUsersIntoListUserWithoutPassword()).thenReturn(Collections.emptyList());

			boolean result = root.createUser();

			assertTrue(result, "createUser() deve restituire true con input valido");

			ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
			verify(mockService).addUserOnData(captor.capture());

			User created = captor.getValue();
			assertEquals("tester", created.getName());
			assertTrue(Hash.verify("secret123", created.getPassword()));
			assertNotEquals("secret123", created.getPassword());

			assertEquals(2, created.getAccessLevelValue());
		}
	}

	// ---------- 4. deleteUser() ----------

	@Test
	void deleteUserWithNullUserListDoesNotThrow() {
		Root root = new Root("pwd");

		try (MockedStatic<UsersUseCase> mockedStatic = mockStatic(UsersUseCase.class)) {
			UsersUseCase mockService = mock(UsersUseCase.class);
			mockedStatic.when(UsersUseCase::getInstance).thenReturn(mockService);

			when(mockService.trasformListUsersIntoListUserWithoutPassword()).thenReturn(null);

			assertDoesNotThrow(root::deleteUser, "deleteUser() non deve lanciare eccezioni se la lista è null");
			verify(mockService).trasformListUsersIntoListUserWithoutPassword();
		}
	}

	@Test
	void deleteUserDeletesExistingUser() throws Exception {
		Root root = new Root("pwd");

		User existing = new User("tester", "pw", AccessLevel.AL1);
		List<User> userList = Collections.singletonList(existing);

		// Sequenza input REALE attesa da Root:
		// - nome utente
		// - conferma nome [s|n]
		// - "Conosci già l'id?" [s|n]
		// - id utente
		setFakeInput("tester", "s", "s", existing.getId());

		// CsvUseCase è null nei unit test (non parte Spring): lo settiamo a mock per
		// evitare NPE
		CsvUseCase csvMock = mock(CsvUseCase.class);
		setCsvUseCaseMock(csvMock);
		doNothing().when(csvMock).deleteAllFileOfUserDeleted(anyString());

		try (MockedStatic<UsersUseCase> mockedStatic = mockStatic(UsersUseCase.class)) {
			UsersUseCase mockService = mock(UsersUseCase.class);
			mockedStatic.when(UsersUseCase::getInstance).thenReturn(mockService);

			// viene chiamato più volte (deleteUser + delUser)
			when(mockService.trasformListUsersIntoListUserWithoutPassword()).thenReturn(userList);

			assertDoesNotThrow(root::deleteUser, "deleteUser() non deve lanciare eccezioni con utente esistente");

			verify(csvMock).deleteAllFileOfUserDeleted(existing.getId());
			verify(mockService).deleteUser(existing);
		}
	}
}
