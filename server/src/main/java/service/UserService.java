package service;

import dataaccess.AuthDAO;
import exception.DataAccessException;
import dataaccess.UserDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.util.Objects;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData registerRequest) throws AlreadyTakenException, DataAccessException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("Error: Expected: register username password email");
        }
        if (!(userDAO.getUser(registerRequest.username()) == null)) {
            throw new AlreadyTakenException("Error: Username already taken. Try again.");
        }
        userDAO.createUser(registerRequest);
        return authDAO.createAuth(registerRequest.username());
    }

    public AuthData login(LoginRequest loginRequest) throws UnauthorizedException {
        UserData userData = userDAO.getUser(loginRequest.username());
        if (userData == null || !userDAO.verifyPassword(userData, loginRequest)) {
            throw new UnauthorizedException("Error: Wrong password or username. Try again");
        }
        return authDAO.createAuth(loginRequest.username());
    }

    public void logout(String logoutRequest) throws UnauthorizedException {
        authDAO.deleteAuth(logoutRequest);
    }

    public void clear() {
        userDAO.clearUsers();
        authDAO.clearAuth();
    }
}
