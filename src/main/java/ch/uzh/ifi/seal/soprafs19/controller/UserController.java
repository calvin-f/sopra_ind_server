package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.UserSetter;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }


    @PostMapping("/users")
    UserSetter createUser(@RequestBody User newUser) {
        try {
            return new UserSetter(this.service.createUser(newUser));
        } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://sopra-fs19-calvin-f-client.herokuapp.com/"})
    @PutMapping ("/users/{id}")
    ResponseEntity<Void> editUser(@PathVariable("id") long id, @RequestBody User newUser) {
        try {
            this.service.editUser(id, newUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
    }

    @GetMapping("/users/{id}")
    UserSetter getUser(@PathVariable("id") long id){
        var user = this.service.getUserById(id);

        if (user != null) {
            return new UserSetter(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

}
