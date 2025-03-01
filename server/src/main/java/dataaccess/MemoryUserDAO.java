package dataaccess;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData createUser(UserData userData) {
        users.put(userData.username(), userData);
        return userData;
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void clearUsers() {
        users.clear();
    }


}
