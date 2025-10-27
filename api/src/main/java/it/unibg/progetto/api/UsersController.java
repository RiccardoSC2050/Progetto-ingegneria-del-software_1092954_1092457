package it.unibg.progetto.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

@RestController
@RequestMapping("/api/users") 
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<Users> getAll() {
        return usersService.getAllUsersFromDataBase();
    }
}
