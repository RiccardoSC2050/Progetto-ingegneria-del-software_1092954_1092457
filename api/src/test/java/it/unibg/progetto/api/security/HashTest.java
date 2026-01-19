package it.unibg.progetto.api.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HashTest {

	@Test
	void hash_returnsNonNull_andNotEqualToPlaintext() {
		String pwd = "Password123!";

		String hashed = Hash.hash(pwd);

		assertNotNull(hashed);
		assertFalse(hashed.trim().isEmpty());
		assertNotEquals(pwd, hashed); // un hash non deve mai essere uguale al plain
	}

	@Test
	void verify_withCorrectPassword_returnsTrue() {
		String pwd = "Password123!";
		String hashed = Hash.hash(pwd);

		assertTrue(Hash.verify(pwd, hashed));
	}

	@Test
	void verify_withWrongPassword_returnsFalse() {
		String pwd = "Password123!";
		String hashed = Hash.hash(pwd);

		assertFalse(Hash.verify("WrongPassword", hashed));
	}

	@Test
	void hash_samePasswordTwice_producesDifferentHashes_butBothVerify() {
		String pwd = "Password123!";

		String h1 = Hash.hash(pwd);
		String h2 = Hash.hash(pwd);

		// BCrypt usa salt, quindi gli hash devono essere diversi
		assertNotEquals(h1, h2);

		// ma entrambi devono validare la password
		assertTrue(Hash.verify(pwd, h1));
		assertTrue(Hash.verify(pwd, h2));
	}
}
