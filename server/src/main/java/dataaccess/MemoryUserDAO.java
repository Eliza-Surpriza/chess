package dataaccess;

import model.LoginRequest;
import model.UserData;


import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, UserData> users = new HashMap<>();

    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void clearUsers() {
        users.clear();
    }

    public boolean verifyPassword(UserData userData, LoginRequest loginRequest) {
        return !Objects.equals(userData.password(), loginRequest.password());
    }

}
