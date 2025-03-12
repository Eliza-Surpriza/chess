package dataaccess;

import exception.DataAccessException;
import model.LoginRequest;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO extends SQLDAO implements UserDAO {

    public SQLUserDAO() {
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    public void clearUsers() {
        clearTable("users");
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

    public boolean verifyPassword(UserData userData, LoginRequest loginRequest) {
        String plainPassword = loginRequest.password();
        String hashedPassword = userData.password();
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
