package it.unibg.progetto.api.operators;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.application.ApiMain;
import it.unibg.progetto.api.components.GlobalScaner;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiMain.class)
class RootCliIntegrationTest {

    @Test
    void createUserFromConsoleFlowWorks() {
        Root root = Root.getInstanceRoot();

        // Input finto per la createUser():
        // nome, password, livello accesso
        String fakeInput = String.join("\n",
                "cliuser",   // name
                "secret",    // password
                "2"          // access level
        ) + "\n";

        System.setIn(new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8)));
        GlobalScaner.scanner = new Scanner(System.in);

        ActionOnUseRS service = ActionOnUseRS.getInstance();
        List<User> beforeList = service.trasformListUsersIntoListUserWithoutPassword();
        long beforeCount = beforeList == null ? 0 :
                beforeList.stream().filter(u -> "cliuser".equalsIgnoreCase(u.getName())).count();

        root.createUser();

        List<User> afterList = service.trasformListUsersIntoListUserWithoutPassword();
        long afterCount = afterList == null ? 0 :
                afterList.stream().filter(u -> "cliuser".equalsIgnoreCase(u.getName())).count();

        // Deve esistere almeno un utente "cliuser" nel DB di test
        assertTrue(afterCount >= 1, "Dopo createUser() deve esistere almeno un utente 'cliuser'");
        assertTrue(afterCount >= beforeCount, "Il numero di 'cliuser' non deve diminuire dopo createUser()");
    }

    @Test
    void deleteUserFromConsoleFlowWorks() {
        Root root = Root.getInstanceRoot();
        ActionOnUseRS service = ActionOnUseRS.getInstance();

        // 1) Mi assicuro che esista un utente di prova "cliuser-del"
        String createInput = String.join("\n",
                "cliuser-del",
                "secret",
                "2"
        ) + "\n";

        System.setIn(new ByteArrayInputStream(createInput.getBytes(StandardCharsets.UTF_8)));
        GlobalScaner.scanner = new Scanner(System.in);
        root.createUser();

        // 2) Ricavo il suo id dal DB di test
        List<User> userList = service.trasformListUsersIntoListUserWithoutPassword();
        String idToDelete = null;
        if (userList != null) {
            for (User u : userList) {
                if ("cliuser-del".equalsIgnoreCase(u.getName())) {
                    idToDelete = u.getId();
                    break;
                }
            }
        }
        assertNotNull(idToDelete, "Deve esistere un utente 'cliuser-del' da eliminare");

        // 3) Input finto per deleteUser():
        // nome → conferma nome → conferma di conoscere l'id → id
        String deleteInput = String.join("\n",
                "cliuser-del",   // nome inserito
                "y",             // confermo che è l'utente giusto
                "y",             // dico che conosco l'id
                idToDelete       // id da eliminare
        ) + "\n";

        System.setIn(new ByteArrayInputStream(deleteInput.getBytes(StandardCharsets.UTF_8)));
        GlobalScaner.scanner = new Scanner(System.in);

        root.deleteUser();

        // 4) Controllo che non esista più nessun "cliuser-del"
        List<User> afterList = service.trasformListUsersIntoListUserWithoutPassword();
        boolean stillPresent = false;
        if (afterList != null) {
            stillPresent = afterList.stream()
                    .anyMatch(u -> "cliuser-del".equalsIgnoreCase(u.getName()));
        }
        assertFalse(stillPresent, "Dopo deleteUser() l'utente 'cliuser-del' non deve più esistere");
    }
}
