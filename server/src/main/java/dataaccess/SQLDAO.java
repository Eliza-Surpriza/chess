package dataaccess;

import exception.DataAccessException;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLDAO {
    public SQLDAO() {
        configureDatabase();
    }

    int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
            for (var i = 0; i < params.length; i++) {
                updateInnerLoop(ps, i, params);
            }
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;

        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    void updateInnerLoop(java.sql.PreparedStatement ps, int i, Object... params) throws SQLException {
        var param = params[i];
        switch (param) {
            case String p -> ps.setString(i + 1, p);
            case Integer p -> ps.setInt(i + 1, p);
            case null -> ps.setNull(i + 1, NULL);
            default -> {
            }
        }
    }

    void clearTable(String table) {
        var statement = "TRUNCATE " + table;
        executeUpdate(statement);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS  tokens (
              `authToken` varchar(256) NOT NULL,
              `authData` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authData)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS  games (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` varchar(2000) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameName)
            )
            """
    };

    //ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
