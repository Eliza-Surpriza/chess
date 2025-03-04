package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {

    private final MemoryUserDAO userDAO;
    private final MemoryAuthDAO authDAO;

    public UserService(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData registerRequest) throws AlreadyTakenException {
        if(registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if(!(userDAO.getUser(registerRequest.username()) == null)) {
            throw new AlreadyTakenException("Error: already taken"); // error code 403
        }
        userDAO.createUser(registerRequest);
        return authDAO.createAuth(registerRequest.username());
    }
    public AuthData login(UserData loginRequest) throws UnauthorizedException{
        UserData userData = userDAO.getUser(loginRequest.username());
        if(!Objects.equals(userData.password(), loginRequest.password())) {
            throw new UnauthorizedException("Error: unauthorized"); // error code 401
        }
        return authDAO.createAuth(loginRequest.username());
    }
    public void logout(String logoutRequest) throws UnauthorizedException{
        if(!(authDAO.getAuth(logoutRequest) == null)) {
            throw new UnauthorizedException("Error: unauthorized"); // error code 401
        }
        authDAO.deleteAuth(logoutRequest);
    }
}
