package it.unibg.progetto.api.operators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.application.ApiMain;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiMain.class)
class RootTest {

    private Root root;

    @BeforeEach
    void setUp() {
        // Inizializzo sempre la Root prima di ogni test
        root = Root.getInstanceRoot();
        assertNotNull(root, "Root.getInstanceRoot() non deve mai restituire null");
    }

    // ---------- 1. Costruttore e Singleton ----------

    @Test
    void getInstanceRootReturnsSameInstance() {
        Root r1 = Root.getInstanceRoot();
        Root r2 = Root.getInstanceRoot();

        // Deve essere la stessa istanza (pattern Singleton)
        assertSame(r1, r2, "getInstanceRoot() deve restituire sempre la stessa istanza");
    }

    @Test
    void getInstanceRootInitializesRootWithFixedValues() {
        // Nome fissato per Root dal costruttore di Operator
        assertEquals("ROOT", root.getName(), "Il nome di Root deve essere 'ROOT'");

        // Id e livello di accesso fissi per l'amministratore
        assertEquals("0", root.getId(), "L'id di Root deve essere '0'");
        assertEquals(5, root.getAccessLevelValue(), "Il livello di accesso di Root deve essere 5");

        // AccessLevel "oggetto" non viene impostato esplicitamente
        assertNull(root.getAccessLevel(), "AccessLevel non viene impostato esplicitamente per Root");

        // La password ora arriva dal database: verifichiamo solo che esista e non sia vuota
        assertNotNull(root.getPassword(), "La password di Root non deve essere null");
        assertFalse(root.getPassword().isBlank(), "La password di Root non deve essere vuota");
    }

    @Test
    void manualRootConstructorSetsIdZeroAndLevelFive() {
        Root customRoot = new Root("pwd");

        // Nome e password presi dal costruttore
        assertEquals("ROOT", customRoot.getName(), "Il nome di Root deve essere 'ROOT'");
        assertEquals("pwd", customRoot.getPassword(), "La password passata al costruttore deve essere mantenuta");

        // Id e livello di accesso fissati per Root
        assertEquals("0", customRoot.getId(), "L'id di Root deve essere '0'");
        assertEquals(5, customRoot.getAccessLevelValue(), "Il livello di accesso di Root deve essere 5");
    }

    // ---------- 2. Metodi DataControl (vuoti ma coperti) ----------

    @Test
    void readDataFileDoesNotThrow() {
        assertDoesNotThrow(root::readDataFile, "readDataFile() non deve lanciare eccezioni");
    }

    @Test
    void createDataFileDoesNotThrow() {
        assertDoesNotThrow(root::createDataFile, "createDataFile() non deve lanciare eccezioni");
    }

    @Test
    void deleteDataFileDoesNotThrow() {
        assertDoesNotThrow(root::deleteDataFile, "deleteDataFile() non deve lanciare eccezioni");
    }
}
