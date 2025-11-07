package it.unibg.progetto.api.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import it.unibg.progetto.api.dto.Userdto;
import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

@RestController
@RequestMapping("/api/users")
public class UsersController {
	
	private final UsersService service;
	private final UserMapper userMapper;

	public UsersController(UsersService service, UserMapper userMapper) {
		this.service = service;
		this.userMapper = userMapper;
		
	}

	//now unusable 
	@GetMapping
	public List<Users> getAll() {
		return service.getAllUsersFromDataBase();
	}

	@PostMapping
	public Userdto create(@RequestBody Userdto userdto) {
		Users users = userMapper.toEntityUsersFromUserdto(userdto);
		Users saved = service.createUser(users);
		return userdto = userMapper.toUserdtoFromUsers(saved);
	}

}
