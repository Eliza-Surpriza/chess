package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.UnauthorizedException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    UserService userService = new UserService(userDAO, authDAO);
    AuthService authService = new AuthService(authDAO);


    @Test
    void authorize() throws DataAccessException {
        userService.register(new UserData("felicity", "1774", "fmerriman@gmail.com"));
        AuthData result = userService.login(new LoginRequest("felicity", "1774"));
        userService.clear();
        assertThrows(UnauthorizedException.class, () -> authService.authorize(result.authToken()));
    }
}