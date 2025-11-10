package it.unibg.progetto.api.operators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.application.ApiMain;
import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.api.operators.AccessLevel;
import it.unibg.progetto.api.operators.InvalidAccessLevelException;
import it.unibg.progetto.api.operators.Operator;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.api.operators.User;
import it.unibg.progetto.service.UsersService;

/**
 * sottoclassi di operators
 */

@ActiveProfiles("test")
@SpringBootTest(classes=ApiMain.class)

public class OperatorsTest {

	
@Autowired ActionOnUseRS conversionUseRS;

private Root root;
private User user;

@BeforeEach
void setUp() throws InvalidAccessLevelException {
  // create object for each test
	
  root = Root.getInstanceRoot(conversionUseRS);
  root.createUser("user", "pw", 2);
  
}

@Test
void FlagLoginTest() throws InvalidAccessLevelException {
		
		assertFalse(root.isFlagLogin());
		assertFalse(user.isFlagLogin());
	
	}}

