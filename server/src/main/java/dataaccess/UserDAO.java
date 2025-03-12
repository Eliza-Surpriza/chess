package dataaccess;

import exception.DataAccessException;
import model.UserData;

public interface UserDAO {
    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username);

    void clearUsers();
}
