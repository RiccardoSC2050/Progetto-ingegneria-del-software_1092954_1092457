package it.unibg.progetto.api.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.Checks;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiMain.class)
class MasterTest {

    @Autowired
    private UsersService usersService;   // per popolare/leggere il DB vero

    private Master master;

    @BeforeEach
    void setUp() {
        // 1) pulisco il DB users prima di ogni test
        List<Users> all = usersService.getAllUsersFromDataBase();
        for (Users u : all) {
            usersService.deleteUsers(u);
        }

        // 2) azzero anche la sessione eventuale
        ManagerSession.logout();

        // 3) creo un'istanza "pulita" di Master
        master = Master.getIstance();
    }

    // ---------- 1) login con credenziali corrette ----------

    @Test
    void login_conCredenzialiCorrette_restituisceAffermativeESettaLaSessione() {
        // preparo un utente nel DB (entity Users -> tabella "users")
        Users entity = new Users(
                "id-master-login",
                "franco",
                "segretissima",
                AccessLevel.AL1.getLevel()
        );
        usersService.addUsersIntoDataUsers(entity);

        // eseguo il login tramite Master (che internamente usa ActionOnUseRS + ManagerSession)
        Checks result = master.login("franco", "segretissima");

        // 1) controllo il valore di ritorno
        assertEquals(Checks.affermative, result);

        // 2) controllo che la sessione sia stata creata correttamente
        Session current = ManagerSession.getCurrent();
        assertNotNull(current, "Dopo login affermative la sessione non dovrebbe essere null");
        assertEquals("id-master-login", current.getUuid());
        assertEquals("mario", current.getName());
        assertEquals(AccessLevel.AL1.getLevel(), current.getAccessLevel());
    }

    // ---------- 2) login con password sbagliata ----------

    @Test
    void login_passwordErrata_restituisceNegativeENonSettaSessione() {
        // utente valido nel DB
        Users entity = new Users(
                "id-master-login2",
                "franco",
                "segretissima",
                AccessLevel.AL1.getLevel()
        );
        usersService.addUsersIntoDataUsers(entity);

        // provo login con password sbagliata
        Checks result = master.login("franco", "password-sbagliata");

        // deve tornare NEGATIVE
        assertEquals(Checks.negative, result);

        // e la sessione deve rimanere null
        assertNull(ManagerSession.getCurrent(), "Con login fallito non deve essere creata nessuna sessione");
    }

    // ---------- 3) login con DB vuoto (utente SECRET) ----------

    @Test
    void login_dbVuoto_restituisceNeutralENonSettaSessione() {
        // DB users è vuoto grazie a @BeforeEach

        Checks result = master.login("qualunque", "qualunque");

        // Se ActionOnUseRS non trova utenti, ritorna l'utente "secret"
        // e Master deve rispondere NEUTRAL
        assertEquals(Checks.neutral, result);

        // In questo caso Master NON deve chiamare ManagerSession.login
        assertNull(ManagerSession.getCurrent(), "Con utente secret la sessione non deve essere impostata");
    }

    // ---------- 4) logout ----------

    @Test
    void logout_azzeraLaSessioneCorrente() {
        // preparo utente nel DB
        Users entity = new Users(
                "id-master-logout",
                "luca",
                "ciao",
                AccessLevel.AL2.getLevel()
        );
        usersService.addUsersIntoDataUsers(entity);

        // faccio login per creare la sessione
        Checks result = master.login("luca", "ciao");
        assertEquals(Checks.affermative, result);
        assertNotNull(ManagerSession.getCurrent(), "Dopo login ci deve essere una sessione");

        // ora faccio logout
        master.logout();

        // e controllo che la sessione sia stata azzerata
        assertNull(ManagerSession.getCurrent(), "Dopo logout la sessione deve essere null");
    }

    // ---------- 5) (facoltativo) RootInData non lancia eccezioni ----------

    @Test
    void rootInData_nonLanciaEccezioni() {
        assertDoesNotThrow(() -> master.RootInData());
    }
}
