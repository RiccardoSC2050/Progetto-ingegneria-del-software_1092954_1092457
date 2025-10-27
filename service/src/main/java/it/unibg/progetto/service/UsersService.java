package it.unibg.progetto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unibg.progetto.data.Users;
import it.unibg.progetto.data.UsersRepository;

@Service
public class UsersService {

	private final UsersRepository repository;

	public UsersService(UsersRepository repository) {
		super();
		this.repository = repository;
	}
	
	public List<Users> getAllUsersFromDataBase() {
		return repository.findAll();
	}
}
