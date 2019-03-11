package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LoginService {

    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Boolean allowedToLogin(User user){
        return this.userRepository.findByUsername(user.getUsername()).getPassword().equals(user.getPassword());
    }

    public User login(User user){
        var loggedInUser = this.userRepository.findByUsername(user.getUsername());
        loggedInUser.setToken(UUID.randomUUID().toString());
        loggedInUser.setStatus(UserStatus.ONLINE);
        return loggedInUser;
    }
}
