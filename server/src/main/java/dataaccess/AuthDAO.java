package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData createAuth(String username);
    AuthData getAuth(String authData);
    void deleteAuth(String authData);
    void clearAuth();
}
