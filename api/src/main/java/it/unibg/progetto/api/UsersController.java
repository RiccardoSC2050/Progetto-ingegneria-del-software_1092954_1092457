package it.unibg.progetto.api;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import  it.unibg.progetto.data.Users;
import  it.unibg.progetto.service.UsersService;

@RestController 
@RequestMapping("/api/users") 
public class UsersController {
    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping 
    public List<Users> getAll() { return service.getAllUsersFromDataBase(); }

}
