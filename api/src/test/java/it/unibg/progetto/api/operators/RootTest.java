package it.unibg.progetto.api.operators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.dto.Rootdto;

class RootTest {

    /**
     * Prima di ogni test azzero il singleton Root.root,
     * così ogni test parte “pulito”.
     */
    @BeforeEach
    void resetSingleton() throws Exception {
        Field field = Root.class.getDeclaredField("root");
        field.setAccessible(true);
        field.set(null, null);   // static field → obj = null
    }

    // ---------- 1. Singleton: getInstanceRoot() ----------

    @Test
    void getInstanceRootReturnsSameInstance() {
        try (MockedStatic<ActionOnUseRS> mockedStatic = mockStatic(ActionOnUseRS.class)) {
            ActionOnUseRS mockService = mock(ActionOnUseRS.class);
            mockedStatic.when(ActionOnUseRS::getInstance).thenReturn(mockService);

            Rootdto dto = new Rootdto();
            dto.setPassword("pwd");                        // password finta
            when(mockService.rootIsOnData()).thenReturn(dto);

            Root r1 = Root.getInstanceRoot();
            Root r2 = Root.getInstanceRoot();

            // Deve essere la stessa istanza (pattern Singleton)
            assertSame(r1, r2, "getInstanceRoot() deve restituire sempre la stessa istanza");
        }
    }

    @Test
    void getInstanceRootInitializesRootWithFixedValues() {
        try (MockedStatic<ActionOnUseRS> mockedStatic = mockStatic(ActionOnUseRS.class)) {
            ActionOnUseRS mockService = mock(ActionOnUseRS.class);
            mockedStatic.when(ActionOnUseRS::getInstance).thenReturn(mockService);

            Rootdto dto = new Rootdto();
            dto.setPassword("pwd");                        // password finta
            when(mockService.rootIsOnData()).thenReturn(dto);

            Root root = Root.getInstanceRoot();

            // Nome fissato per Root dal costruttore di Operator
            assertEquals("ROOT", root.getName(), "Il nome di Root deve essere 'ROOT'");

            // Id e livello di accesso fissi per l'amministratore
            assertEquals("0", root.getId(), "L'id di Root deve essere '0'");
            assertEquals(5, root.getAccessLevelValue(), "Il livello di accesso di Root deve essere 5");

            // AccessLevel "oggetto" non viene impostato esplicitamente
            assertNull(root.getAccessLevel(), "AccessLevel non viene impostato esplicitamente per Root");

            // La password arriva dal DTO mockato
            assertEquals("pwd", root.getPassword(), "La password deve essere quella letta dal DTO");
        }
    }

    // ---------- 2. Costruttore manuale ----------

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

    // ---------- 3. Metodi DataControl (vuoti ma coperti) ----------

    @Test
    void readDataFileDoesNotThrow() {
        Root root = new Root("pwd");
        assertDoesNotThrow(root::readDataFile, "readDataFile() non deve lanciare eccezioni");
    }

    @Test
    void createDataFileDoesNotThrow() {
        Root root = new Root("pwd");
        assertDoesNotThrow(root::createDataFile, "createDataFile() non deve lanciare eccezioni");
    }

    @Test
    void deleteDataFileDoesNotThrow() {
        Root root = new Root("pwd");
        assertDoesNotThrow(root::deleteDataFile, "deleteDataFile() non deve lanciare eccezioni");
    }

    // ---------- 4. createUser() ----------

    @Test
    void createUserCallsAddUserOnDataOnValidInput() {
        Root root = new Root("pwd");

        // Input finto per la console:
        // - nome = tester
        // - password = secret
        // - livello = 2
        String fakeInput = String.join("\n",
                "tester",   // nome utente
                "secret",   // password in chiaro
                "2"         // livello di accesso
        ) + "\n";

        System.setIn(new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8)));
        GlobalScaner.scanner = new Scanner(System.in);

        try (MockedStatic<ActionOnUseRS> mockedStatic = mockStatic(ActionOnUseRS.class)) {
            ActionOnUseRS mockService = mock(ActionOnUseRS.class);
            mockedStatic.when(ActionOnUseRS::getInstance).thenReturn(mockService);

            // Nessun utente pre-esistente con quel nome
            when(mockService.trasformListUsersIntoListUserWithoutPassword())
                    .thenReturn(Collections.emptyList());

            boolean result = root.createUser();

            assertTrue(result, "createUser() deve restituire true con input valido");
            // Verifico che abbia chiamato il salvataggio di un utente
            verify(mockService).addUserOnData(any(User.class));
        }
    }

    // ---------- 5. deleteUser() ----------

    @Test
    void deleteUserWithNullUserListDoesNotThrow() {
        Root root = new Root("pwd");

        try (MockedStatic<ActionOnUseRS> mockedStatic = mockStatic(ActionOnUseRS.class)) {
            ActionOnUseRS mockService = mock(ActionOnUseRS.class);
            mockedStatic.when(ActionOnUseRS::getInstance).thenReturn(mockService);

            // Caso limite: lista utenti null
            when(mockService.trasformListUsersIntoListUserWithoutPassword())
                    .thenReturn(null);

            assertDoesNotThrow(root::deleteUser,
                    "deleteUser() non deve lanciare eccezioni se la lista è null");
            verify(mockService).trasformListUsersIntoListUserWithoutPassword();
        }
    }

    @Test
    void deleteUserDeletesExistingUser() {
        Root root = new Root("pwd");

        // Creo un utente finto
        User existing = new User("tester", "pw", AccessLevel.AL1);
        List<User> userList = Collections.singletonList(existing);

        // Input finto per:
        // - scegliere "tester"
        // - confermare 'y'
        // - dire che conosci l'id 'y'
        // - inserire l'id corretto
        String fakeInput = String.join("\n",
                "tester",            // nome utente da eliminare
                "y",                 // conferma "è l'utente corretto?"
                "y",                 // "Conosci già l'id dell'utente? [y|n]"
                existing.getId()     // id utente
        ) + "\n";

        System.setIn(new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8)));
        GlobalScaner.scanner = new Scanner(System.in);

        try (MockedStatic<ActionOnUseRS> mockedStatic = mockStatic(ActionOnUseRS.class)) {
            ActionOnUseRS mockService = mock(ActionOnUseRS.class);
            mockedStatic.when(ActionOnUseRS::getInstance).thenReturn(mockService);

            // La lista utenti contiene il nostro 'existing'
            when(mockService.trasformListUsersIntoListUserWithoutPassword())
                    .thenReturn(userList);

            assertDoesNotThrow(root::deleteUser,
                    "deleteUser() non deve lanciare eccezioni con utente esistente");

            // Verifico che abbia chiamato la delete sul service con l'utente giusto
            verify(mockService).deleteUser(existing);
        }
    }
}
