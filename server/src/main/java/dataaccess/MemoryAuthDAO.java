package dataaccess;

import model.AuthData;

import java.util.UUID;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> tokens = new HashMap<>();


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuth(String username) {
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, username);
        tokens.put(authToken, authData);
        return authData;
    }

    public AuthData getAuth(String authToken) {
        return tokens.get(authToken);
    }

    public void deleteAuth(String authToken) {
        tokens.remove(authToken);
    }

    public void clearAuth() {
        tokens.clear();
    }


}
