package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    GameService gameService = new GameService(gameDAO, authDAO);
    MemoryUserDAO userDAO = new MemoryUserDAO();
    UserService userService = new UserService(userDAO, authDAO);

    @BeforeEach
    void setUp() {
        AuthData kitResult = userService.register(new UserData("kit", "1934", "robinhood@gmail.com"));
        gameService.createGame(new CreateRequest("penguin"));
        gameService.joinGame(new JoinRequest("WHITE", 1, kitResult.authToken()));
    }

    @Test
    void createGame() {
        CreateResult createResult = gameService.createGame(new CreateRequest("sky"));
        assertEquals(1, createResult.gameID());
    }

    @Test
    void createGameBadRequest() {
        assertThrows(BadRequestException.class, () -> gameService.createGame(new CreateRequest(null)));
    }

    @Test
    void joinGame() {
        // join penguin game with black player name
        // somehow check that it happened
        AuthData felicityData = userService.register(new UserData("felicity", "1774", "fmerriman@gmail.com"));
        gameService.joinGame(new JoinRequest("BLACK", 1, felicityData.authToken()));
        GameData gameData = gameDAO.getGame(1);
        assertEquals("felicity", gameData.blackUsername());
    }

    @Test
    void joinGameAlreadyTaken() {
        // join penguin game with white player name
        AuthData felicityData = userService.register(new UserData("felicity", "1774", "fmerriman@gmail.com"));
        assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(new JoinRequest("WHITE", 1, felicityData.authToken())));
    }
}