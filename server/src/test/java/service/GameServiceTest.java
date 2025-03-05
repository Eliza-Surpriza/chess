package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import model.CreateResult;
import model.CreateRequest;
import model.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    GameService gameService = new GameService(gameDAO, authDAO);

    @BeforeEach
    void setUp() {
        gameService.createGame(new CreateRequest("penguin"));
        gameService.joinGame(new JoinRequest("WHITE", 1, "polarbear"));
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
    }

    @Test
    void joinGameAlreadyTaken() {
        // join penguin game with white player name
    }
}