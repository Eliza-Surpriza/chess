package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {

    private final MemoryUserDAO userDAO;
    private final MemoryAuthDAO authDAO;

    public UserService(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData registerRequest) throws DataAccessException {
        if(!(userDAO.getUser(registerRequest.username()) == null)) {
            throw new DataAccessException("Error: already taken"); // error code 403
        }
        // not sure how to do the other errors
        userDAO.createUser(registerRequest);
        return authDAO.createAuth(registerRequest.username());
    }
    public AuthData login(UserData loginRequest) throws DataAccessException{

    }
    public void logout(String logoutRequest) throws DataAccessException{

    }
}
