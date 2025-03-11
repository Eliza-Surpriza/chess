package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO extends SQLDAO implements UserDAO {

    public SQLUserDAO() throws SQLException, DataAccessException {
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO pet (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    public UserData getUser(String username) {
        return null;
    }

    public void clearUsers() {

    }
}
