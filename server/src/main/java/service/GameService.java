package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
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
        return new CreateResult(gameData.GameID());
    }
//    public GameData createGame(CreateRequest createRequest) throws UnauthorizedException {
////        UserData userData = userDAO.getUser(loginRequest.username());
////        if (userData == null || !Objects.equals(userData.password(), loginRequest.password())) {
////            throw new UnauthorizedException("Error: unauthorized"); // error code 401
////        }
////        return authDAO.createAuth(loginRequest.username());
//    }


}
