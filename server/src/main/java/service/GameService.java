package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import model.*;

import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateResult createGame(CreateRequest createRequest) throws BadRequestException {
        if (createRequest.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData gameData = gameDAO.createGame(createRequest.gameName());
        return new CreateResult(gameData.gameID());
    }

    public void joinGame(JoinRequest joinRequest) {
        try {
            GameData gameData = gameDAO.getGame(joinRequest.gameID());
            String username = authDAO.getAuth(joinRequest.authToken()).username();
            if (joinRequest.playerColor() == null || joinRequest.authToken() == null || gameData == null) {
                throw new BadRequestException("Expected: join id color");
            }
            GameData newData = addPlayer(joinRequest, username, gameData);
            gameDAO.updateGame(newData);
        } catch (Exception e) {
            throw new BadRequestException("Expected: join id color");
        }
    }

    private GameData addPlayer(JoinRequest joinRequest, String username, GameData gameData) {
        GameData newData;
        if (joinRequest.playerColor().equals("WHITE")) {
            if (Objects.equals(gameData.whiteUsername(), null)) {
                newData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                throw new AlreadyTakenException("white player is already claimed");
            }
        } else if (joinRequest.playerColor().equals("BLACK")) {
            if (Objects.equals(gameData.blackUsername(), null)) {
                newData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            } else {
                throw new AlreadyTakenException("black player is already claimed");
            }
        } else {
            throw new BadRequestException("Expected: join id color (color options are \"white\" or \"black\")");
        }
        return newData;
    }

    public ListResult listGames() {
        return new ListResult(gameDAO.listGames());
    }

    public void clear() {
        gameDAO.clearGames();
    }


}
