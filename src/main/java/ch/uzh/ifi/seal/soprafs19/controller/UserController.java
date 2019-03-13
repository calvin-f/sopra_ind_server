package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.UserSetter;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.Media;

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
    @ResponseStatus(HttpStatus.CREATED)
    UserSetter createUser(@RequestBody User newUser) {
        try {
            return new UserSetter(this.service.createUser(newUser));
        } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://sopra-fs19-calvin-f-client.herokuapp.com/"})
    @PutMapping ("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> editUser(@PathVariable("id") long id, @RequestBody User newUser) {
        try {
            this.service.editUser(id, newUser);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping("/users/{id}")
    @ResponseStatus (HttpStatus.OK)
    UserSetter getUser(@PathVariable("id") long id){
        var user = this.service.getUserById(id);

        if (user != null) {
            return new UserSetter(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping ("/users/{id}")
    @ResponseStatus (HttpStatus.OK)
    boolean canEdit(@PathVariable("id") long id, @RequestBody String token) {
        boolean result = this.service.canEdit(id, token);
        return result;
    }

}
