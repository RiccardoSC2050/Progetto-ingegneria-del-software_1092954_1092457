package it.unibg.progetto.api.domain.operators;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.application.usecase.UsersUseCase;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.User;


@ActiveProfiles("test")
@SpringBootTest


class RootCliIntegrationTest {

    private InputStream originalIn;
    private Scanner originalScanner;

    @BeforeEach
    void setup() {
        originalIn = System.in;
        originalScanner = GlobalScanner.scanner;
    }

    @AfterEach
    void teardown() {
        System.setIn(originalIn);

        if (GlobalScanner.scanner != null && GlobalScanner.scanner != originalScanner) {
            try { GlobalScanner.scanner.close(); } catch (Exception ignored) {}
        }
        GlobalScanner.scanner = originalScanner;
    }

    private void setFakeInput(String... lines) {
        String fakeInput = String.join("\n", lines) + "\n";
        System.setIn(new ByteArrayInputStream(fakeInput.getBytes(StandardCharsets.UTF_8)));
        GlobalScanner.scanner = new Scanner(System.in);
    }

    @Test
    @Timeout(10)
    void createUserFromConsoleFlowWorks() {
        
        Root root = new Root("pwd");

        String uname = "cliuser_" + UUID.randomUUID().toString().substring(0, 8);

        
        setFakeInput(uname, "secret123", "2");

        assertTrue(root.createUser());

        UsersUseCase service = UsersUseCase.getInstance();
        List<User> users = service.trasformListUsersIntoListUserWithoutPassword();

        assertNotNull(users);
        assertTrue(users.stream().anyMatch(u -> uname.equals(u.getName())));
    }

    @Test
    @Timeout(10)
    void deleteUserFromConsoleFlowWorks() {
        Root root = new Root("pwd");
        UsersUseCase service = UsersUseCase.getInstance();

        String uname = "cliuserdel_" + UUID.randomUUID().toString().substring(0, 8);

        
        setFakeInput(uname, "secret123", "2");
        assertTrue(root.createUser());

       
        List<User> users = service.trasformListUsersIntoListUserWithoutPassword();
        assertNotNull(users);

        String idToDelete = users.stream()
                .filter(u -> uname.equals(u.getName()))
                .findFirst()
                .map(User::getId)
                .orElse(null);

        assertNotNull(idToDelete);

        
        setFakeInput(uname, "s", "s", idToDelete);
        assertDoesNotThrow(root::deleteUser);

        
        List<User> after = service.trasformListUsersIntoListUserWithoutPassword();
        assertTrue(after == null || after.stream().noneMatch(u -> uname.equals(u.getName())));
    }
}
