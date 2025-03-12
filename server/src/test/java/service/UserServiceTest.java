package service;

import dataaccess.*;
import exception.DataAccessException;
import exception.AlreadyTakenException;
import exception.UnauthorizedException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();
    UserService userService = new UserService(userDAO, authDAO);


    @BeforeEach
    void setUp() throws DataAccessException {
        userService.register(new UserData("felicity", "1774", "fmerriman@gmail.com"));
    }

    @AfterEach
    void cleanUp() {
        userService.clear();
    }

    @Test
    void register() throws DataAccessException {
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
        // sign in with username "felicity" password "1774"
        AuthData result = userService.login(new LoginRequest("felicity", "1774"));
        assertEquals("felicity", result.username());
    }

    @Test
    void loginUnauthorized() {
        // sign in with username "felicity" password "wrong_password" get error
        assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("felicity", "wrong!")));

    }

    @Test
    void loginWrongUsername() {
        assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("wrong!", "1774")));

    }

    @Test
    void clear() {
        userService.login(new LoginRequest("felicity", "1774"));
        userService.clear();
        assertThrows(UnauthorizedException.class, () -> userService.login(new LoginRequest("felicity", "1774")));
    }
}