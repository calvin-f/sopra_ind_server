package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    protected User createdUser;

    @Before
    public void setUp() throws Exception {
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("1224");
        testUser.setBirthday(new Date());
        createdUser = userService.createUser(testUser);
    }

    @Test
    public void allowedToLogin() {
        Assert.assertTrue(loginService.allowedToLogin(userRepository.findByUsername("testUsername")));
    }

    @Test
    public void login() {
        User loggedInUser = loginService.login(userRepository.findByUsername("testUsername"));

        Assert.assertEquals(loggedInUser.getStatus(), UserStatus.ONLINE);
        Assert.assertNotNull(loggedInUser.getToken());
    }

    @After
    public void tearDown() throws Exception {
        userRepository.delete(createdUser);
    }
}