package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import exception.UnauthorizedException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import com.google.gson.Gson;
import websocket.messages.ErrorMessage;

import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;


public class websocketHandler  {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final Gson gson = new Gson();

    public websocketHandler (UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = userDAO.getUser(command.getAuthToken()).username();

//            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leave(session, username, command);
                case RESIGN -> resign(session, username, command);
            }
        } catch (UnauthorizedException ex) {
            // Serializes and sends the error message
//            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
//            ex.printStackTrace();
//            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    // define connect, make move, leave, resign

    public void connect(Session session, String username, UserGameCommand command) {
        // um connect I guess? maybe I make a connections class like in petshop. idk.
    }

    public void makeMove(Session session, String username, MakeMoveCommand command) {
        GameData gameData = gameDAO.getGame(command.getGameID());
        try {
            gameData.game().makeMove(command.getMove());
            checkGameStatus(gameData.game());
            gameDAO.updateGame(gameData);
            // load game (for everyone)
            // notify others
        } catch (InvalidMoveException e) {
            // send error message :)
        }
    }

    public void checkGameStatus(ChessGame game) {
        ChessGame.TeamColor otherTeam = (game.currentTeam == WHITE) ? BLACK : WHITE;
        if (game.isInCheck(otherTeam)) {
            // notify players
        }
        if (game.isInCheckmate(otherTeam)) {
            game.endGame();
            // notify players
        }
        if (game.isInStalemate(game.currentTeam)) {
            game.endGame();
            // notify players
        }
        if (game.isInStalemate(otherTeam)) {
            game.endGame();
            // notify players
        }
    }

    public void leave(Session session, String username, UserGameCommand command) {
        GameData gameData = gameDAO.getGame(command.getGameID());
        GameData updated;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            updated = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else if (Objects.equals(gameData.blackUsername(), username)) {
            updated = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        } else {
            updated = gameData;
        }
        gameDAO.updateGame(updated);
        // notify others that username left
        // disconnect from websocket
    }

    public void resign(Session session, String username, UserGameCommand command) {
        GameData gameData = gameDAO.getGame(command.getGameID());
        gameData.game().endGame();
        // notify others that username resigned
    }
    // resign: set game to over. notify others

}
