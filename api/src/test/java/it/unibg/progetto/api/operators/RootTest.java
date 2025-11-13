package it.unibg.progetto.api.operators;

import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.application.ApiMain;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiMain.class)

public class RootTest {

	private Root root;
	private User user;

	@BeforeEach
	void setUp() {
		// create object for each test

		root = Root.getInstanceRoot();
		root.createUser();

	}

}
