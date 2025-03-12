package dataaccess;

import com.google.gson.Gson;
import exception.DataAccessException;
import model.AuthData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO extends SQLDAO implements AuthDAO {
    public SQLAuthDAO() throws SQLException, DataAccessException {
    }

    public AuthData createAuth(String username) {
        var statement = "INSERT INTO tokens (authToken, authData) VALUES (?, ?)";
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, username);
        var json = new Gson().toJson(authData);
        executeUpdate(statement, authToken, json);
        return authData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void deleteAuth(String authToken) {
        var statement = "DELETE FROM tokens WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    public void clearAuth() {
        clearTable("tokens");
    }

    public AuthData getAuth(String authToken) {
        var statement = "SELECT authToken, authData FROM tokens WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);
            return getAuthFromResultSet(ps);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
    }


    private AuthData getAuthFromResultSet(PreparedStatement ps) throws SQLException {
        try (var rs = ps.executeQuery()) {
            if (rs.next()) {
                return readAuth(rs);
            }
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var json = rs.getString("authData");
        var authData = new Gson().fromJson(json, AuthData.class);
        return new AuthData(authToken, authData.username());
    }
}
