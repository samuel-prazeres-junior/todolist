package br.com.cursojava.todolist.user.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.cursojava.todolist.user.domain.UserModel;
import br.com.cursojava.todolist.user.repository.IUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository repository;
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel create(@RequestBody UserModel userModel){
        var user = repository.findByUsername(userModel.getUsername());
        if (user != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        var hashpassowrd = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(hashpassowrd);
        return this.repository.save(userModel);
    }
}
