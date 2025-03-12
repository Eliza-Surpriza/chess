package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData createAuth(String username);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    void clearAuth();
}
