package dataaccess;

import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void getGame() {
        GameData expected = gameDAO.createGame("unicycle");
        GameData observed = gameDAO.getGame(expected.gameID());
        assertEquals(expected, observed);
    }

    @Test
    void listGames() {
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