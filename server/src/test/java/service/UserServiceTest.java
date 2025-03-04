package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.AlreadyTakenException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    UserService userService = new UserService(userDAO, authDAO);

    @BeforeEach
    void setUp() {
        // register username "felicity" password "1774"
        // register user "samantha" password "1904"
        // login user "samantha" password "1904"
        try {
            userService.register(new UserData("felicity", "1774", "fmerriman@gmail.com"));
            userService.register(new UserData("samantha", "1904", "sparkington@gmail.com"));
            userService.login(new UserData("samantha", "1904", "sparkington@gmail.com"));
        } catch(AlreadyTakenException ignored) {}
    }

    @Test
    void register() {
        // register with username "kit" password "1934" email "mkittredge@gmail.com"
        AuthData result = userService.register(new UserData("kit", "1934", "robinhood@gmail.com"));
        assertEquals("kit", result.username());
    }

    @Test
    void registerAlreadyTaken() {
        // try to sign up with username "felicity" password "1774" email "felicitymerriman@mgmail.com" get error
        assertThrows(AlreadyTakenException.class, () -> userService.register(new UserData("felicity", "1774", "fmerriman@gmail.com")));
    }

    @Test
    void login() {
        // sign in with username "felicity" password "wrong_password" get error
        // sign in with username "felicity" password "1774"
    }

    @Test
    void logout() {
        // logout with wrong auth token, get error
        // logout with correct auth token, no error
    }
}