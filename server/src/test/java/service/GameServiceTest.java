package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import model.CreateResult;
import model.CreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    GameService gameService = new GameService(gameDAO, authDAO);

    @BeforeEach
    void setUp() {
    }

    @Test
    void createGame() {
        CreateResult createResult = gameService.createGame(new CreateRequest("penguin"));
        assertEquals(1, createResult.gameID());
    }

    @Test
    void createGameBadRequest() {
        assertThrows(BadRequestException.class, () -> gameService.createGame(new CreateRequest(null)));
    }
}