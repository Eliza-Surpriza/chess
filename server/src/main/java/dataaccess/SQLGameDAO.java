package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.DataAccessException;
import model.GameData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class SQLGameDAO extends SQLDAO implements GameDAO {

    public GameData createGame(String gameName) {
        ChessGame game = new ChessGame();
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(game);
        var id = executeUpdate(statement, "", "", gameName, json); // how to put null in here?
        return new GameData(id, null, null, gameName, game);
    }

    public GameData getGame(int id) {
        var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM games WHERE id=?";
        try (var conn = DatabaseManager.getConnection(); var ps = conn.prepareStatement(statement)) {
            ps.setInt(1, id);
            return getGameFromResultSet(ps);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
    }

    private GameData getGameFromResultSet(PreparedStatement ps) throws SQLException {
        try (var rs = ps.executeQuery()) {
            if (rs.next()) {
                return readGame(rs);
            }
        }
        return null;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var whiteUsername = rs.getString("whiteUsername");
        whiteUsername = Objects.equals(whiteUsername, "") ? null : whiteUsername;
        var blackUsername = rs.getString("blackUsername");
        blackUsername = Objects.equals(blackUsername, "") ? null : blackUsername;
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        var game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(id, whiteUsername, blackUsername, gameName, game);
    }

    public Collection<GameData> listGames() {
        var result = new ArrayList<GameData>();
        var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM games";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(readGame(rs));
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return result;
    }

    public void updateGame(GameData data) {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE id = ?";
        var json = new Gson().toJson(data.game());
        String black = (data.blackUsername() == null) ? "" : data.blackUsername();
        String white = (data.whiteUsername() == null) ? "" : data.whiteUsername();
        executeUpdate(statement, white, black, data.gameName(), json, data.gameID());
    }

    public void clearGames() {
        clearTable("games");
    }
}
