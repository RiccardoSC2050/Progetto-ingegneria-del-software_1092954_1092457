package controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import dto.Userdto;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;
import mapper.UserMapper;

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
		Users users = userMapper.toEntityUsers(userdto);
		Users saved = service.createUser(users);
		return userdto = userMapper.toUser(saved);
	}

}
