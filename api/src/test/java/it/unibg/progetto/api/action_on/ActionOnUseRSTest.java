package it.unibg.progetto.api.action_on;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.application.ApiMain;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.operators.User;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiMain.class)
class ActionOnUseRSTest {

    @Autowired
    private ActionOnUseRS actionOnUseRS;   // bean reale, creato da Spring

    @Autowired
    private UsersService usersService;     // anche questo vero, con H2

    @BeforeEach
    void cleanDatabase() {
        // Puliamo la tabella users prima di ogni test
        List<Users> all = usersService.getAllUsersFromDataBase();
        for (Users u : all) {
            usersService.deleteUsers(u);
        }
    }

    // ---------- 1) Test della conversione User -> Users ----------

    @Test
    void trasformUserIntoUsersEntity_mappaCorrettamenteIValori() {
        User user = new User("id-test", "mario", "pwd123", AccessLevel.AL2);

        Users entity = actionOnUseRS.trasformUserIntoUsersEntity(user);

        assertNotNull(entity);
        assertEquals("id-test", entity.getUuid());
        assertEquals("mario", entity.getName());
        assertEquals("pwd123", entity.getPassword());
        assertEquals(AccessLevel.AL2.getLevel(), entity.getAccessLevel());
    }

    // ---------- 2) Test addUserOnData / deleteUser ----------

    @Test
    void addUserOnData_salvaUtenteNelDatabase() {
        User user = new User("id-add", "anna", "pw", AccessLevel.AL1);

        actionOnUseRS.addUserOnData(user);

        List<Users> all = usersService.getAllUsersFromDataBase();
        assertEquals(1, all.size());

        Users saved = all.get(0);
        assertEquals("id-add", saved.getUuid());
        assertEquals("anna", saved.getName());
        assertEquals("pw", saved.getPassword());
        assertEquals(AccessLevel.AL1.getLevel(), saved.getAccessLevel());
    }

    @Test
    void deleteUser_rimuoveUtenteDalDatabase() {
        User user = new User("id-del", "carlo", "pw", AccessLevel.AL3);
        actionOnUseRS.addUserOnData(user);

        assertFalse(usersService.getAllUsersFromDataBase().isEmpty());

        actionOnUseRS.deleteUser(user);

        assertTrue(usersService.getAllUsersFromDataBase().isEmpty());
    }

    // ---------- 3) Test LoginAuthenticator ----------

    @Test
    void loginAuthenticator_credCorrect_restituisceUtenteProtetto() {
        // preparo un utente nel DB
        User user = new User("id-login", "mario", "segretissima", AccessLevel.AL1);
        actionOnUseRS.addUserOnData(user);

        // provo il login
        User logged = actionOnUseRS.LoginAuthenticator("mario", "segretissima");

        assertNotNull(logged);
        assertEquals("mario", logged.getName());
        // password mascherata da returnProtectedUser
        assertEquals("*********", logged.getPassword());
        assertEquals(AccessLevel.AL1, logged.getAccessLevel());
    }

    @Test
    void loginAuthenticator_passwordErrata_restituisceNull() {
        User user = new User("id-login2", "mario", "segretissima", AccessLevel.AL1);
        actionOnUseRS.addUserOnData(user);

        User logged = actionOnUseRS.LoginAuthenticator("mario", "password-sbagliata");

        assertNull(logged);
    }

    @Test
    void loginAuthenticator_dbVuoto_restituisceUtenteSecret() {
        // DB pulito dal @BeforeEach

        User logged = actionOnUseRS.LoginAuthenticator("qualunque", "qualunque");

        assertNotNull(logged);
        // creato con StrangeValues.secret.toString()
        assertEquals("secret", logged.getName());
        assertEquals("secret", logged.getPassword());
        // accessLevel è null in quel costruttore
        assertNull(logged.getAccessLevel());
    }
}
