package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.UserSetter;
import ch.uzh.ifi.seal.soprafs19.service.LoginService;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;


    LoginController(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    @PostMapping("/login")
    UserSetter login(@RequestBody User user) {
        if (this.userService.existsUserByUsername(user.getUsername())) {
            if (this.loginService.allowedToLogin(user)) {
                return new UserSetter(this.loginService.login(user));
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Username or Password");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please register first");
        }
    }
}