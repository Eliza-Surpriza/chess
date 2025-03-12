package dataaccess;

import exception.DataAccessException;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO extends SQLDAO implements UserDAO {

    public SQLUserDAO() throws SQLException, DataAccessException {
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    public UserData getUser(String username) {
        var statement = "SELECT username, password, email FROM users WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
            var ps = conn.prepareStatement(statement)) {

            ps.setString(1, username);
            return getUserFromResultSet(ps);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
    }

    public void clearUsers() {
        var statement = "TRUNCATE users";
        executeUpdate(statement);
    }


    private UserData getUserFromResultSet(PreparedStatement ps) throws SQLException {
        try (var rs = ps.executeQuery()) {
            if (rs.next()) {
                return readUser(rs);
            }
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

}
