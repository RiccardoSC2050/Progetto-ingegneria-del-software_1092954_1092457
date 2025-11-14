package it.unibg.progetto.api.operators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class RootTest {

	// ---------- 1. Costruttore e singleton ----------

	@Test
	void getInstanceRootReturnsSameInstance() {
		Root r1 = Root.getInstanceRoot();
		Root r2 = Root.getInstanceRoot();

		// Deve essere la stessa istanza (pattern Singleton)
		assertSame(r1, r2, "getInstanceRoot() deve restituire sempre la stessa istanza");
	}

	@Test
	void getInstanceRootInitializesRootWithFixedValues() {
		Root root = Root.getInstanceRoot();

		// Valori fissati nel metodo getInstanceRoot / costruttore di Operator
		assertEquals("ROOT", root.getName());
		assertEquals("1234", root.getPassword());
		assertEquals("0", root.getId());
		assertEquals(5, root.getAccessLevelValue());
		assertNull(root.getAccessLevel(), "AccessLevel non viene impostato esplicitamente per Root");
	}

	@Test
	void manualRootConstructorSetsIdZeroAndLevelFive() {
		Root customRoot = new Root("ADMIN", "pwd");

		// Nome e password presi dal costruttore
		assertEquals("ADMIN", customRoot.getName());
		assertEquals("pwd", customRoot.getPassword());

		// Id e livello di accesso fissati per Root
		assertEquals("0", customRoot.getId());
		assertEquals(5, customRoot.getAccessLevelValue());
	}

	// ---------- 2. Metodi DataControl (vuoti ma coperti) ----------

	@Test
	void readDataFileDoesNotThrow() {
		Root root = Root.getInstanceRoot();
		assertDoesNotThrow(root::readDataFile);
	}

	@Test
	void createDataFileDoesNotThrow() {
		Root root = Root.getInstanceRoot();
		assertDoesNotThrow(root::createDataFile);
	}

	@Test
	void deleteDataFileDoesNotThrow() {
		Root root = Root.getInstanceRoot();
		assertDoesNotThrow(root::deleteDataFile);
	}
}
