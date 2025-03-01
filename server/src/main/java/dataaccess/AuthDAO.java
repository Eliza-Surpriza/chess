package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authData) throws DataAccessException;
    void deleteAuth(String authData) throws DataAccessException;
}
