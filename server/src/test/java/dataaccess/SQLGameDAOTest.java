package dataaccess;

import chess.ChessGame;
import exception.DataAccessException;
import model.GameData;
import model.ListResult;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {
    GameDAO gameDAO = new SQLGameDAO();

    @BeforeEach
    void setUp() {
        gameDAO.createGame("mariposa");
    }

    @AfterEach
    void tearDown() {
        gameDAO.clearGames();
    }

    @Test
    void createGame() {
        GameData expected = gameDAO.createGame("fishbowl");
        GameData observed = gameDAO.getGame(expected.gameID());
        assertEquals(expected, observed);
    }

    @Test
    void createGameBadData() {
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void getGame() {
        GameData expected = gameDAO.createGame("unicycle");
        GameData observed = gameDAO.getGame(expected.gameID());
        assertEquals(expected, observed);
    }

    @Test
    void getGameNotThere() {
        assertNull(gameDAO.getGame(59));
    }

    @Test
    void listGames() {
        GameData penguin = new GameData(1, null, null, "mariposa", new ChessGame());
        GameData watermelon = gameDAO.createGame("fishbowl");
        Collection<GameData> expected = List.of(penguin, watermelon);
        Collection<GameData> actual = gameDAO.listGames();
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    void listGamesNoGames() {
        gameDAO.clearGames();
//        Collection<GameData> expected = new ArrayList<GameData>();
//        Collection<GameData> actual = gameDAO.listGames();
//        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
        assertEquals(new ArrayList<>(), gameDAO.listGames());
    }

    @Test
    void updateGame() {
    }

    @Test
    void clearGames() {
        GameData data = gameDAO.createGame("fishbowl");
        gameDAO.clearGames();
        assertNull(gameDAO.getGame(data.gameID()));
    }
}