package dataaccess;

import exception.DataAccessException;
import model.LoginRequest;
import model.UserData;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username);

    void clearUsers();

    boolean verifyPassword(UserData userData, LoginRequest loginRequest);
}
