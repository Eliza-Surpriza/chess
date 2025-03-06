package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import model.*;

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
        GameData gameData = gameDAO.getGame(joinRequest.gameID());
        String username = authDAO.getAuth(joinRequest.authToken()).username();
        if (joinRequest.playerColor() == null || joinRequest.authToken() == null || gameData == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData newData;
        if (joinRequest.playerColor().equals("WHITE")) {
            if (gameData.whiteUsername() == null) {
                newData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        } else if (joinRequest.playerColor().equals("BLACK")) {
            if (gameData.blackUsername() == null) {
                newData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        } else {
            throw new BadRequestException("Error: bad request");
        }
        gameDAO.updateGame(newData);
    }

    public ListResult listGames() {
        return new ListResult(gameDAO.listGames());
    }

    public void clear() {
        gameDAO.clearGames();
    }


}
