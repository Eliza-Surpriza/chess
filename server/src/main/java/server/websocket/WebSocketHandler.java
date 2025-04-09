package server.websocket;

import chess.ChessGame;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import exception.UnauthorizedException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import com.google.gson.Gson;
import websocket.messages.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

import java.io.IOException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;


public class WebSocketHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final Gson gson = new Gson();
    private final ConnectionManager connections = new ConnectionManager();


    public WebSocketHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            String username = authDAO.getAuth(command.getAuthToken()).username();
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(username, (MakeMoveCommand) command);
                case LEAVE -> leave(username, command);
                case RESIGN -> resign(username, command);
            }
        } catch (UnauthorizedException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ERROR, "Error: Unauthorized");
            String json = gson.toJson(errorMessage);
            session.getRemote().sendString(json);
        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage(ERROR, "Unexpected Error");
            String json = gson.toJson(errorMessage);
            session.getRemote().sendString(json);
        }
    }


    public void connect(Session session, String username, UserGameCommand command) throws IOException {
        connections.add(username, session);
        GameData gameData = gameDAO.getGame(command.getGameID());
        String color;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            color = "white";
        } else if (Objects.equals(gameData.blackUsername(), username)) {
            color = "black";
        } else {
            color = "observer";
        }
        String message = username + "joined game as " + color;
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(username, notificationMessage, false);
        LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, gameData.game());
        connections.rootMessage(username, loadGameMessage);
    }

    public void makeMove(String username, MakeMoveCommand command) throws IOException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        try {
            gameData.game().makeMove(command.getMove());
            checkGameStatus(gameData, username);
            gameDAO.updateGame(gameData);
            LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, gameData.game());
            connections.broadcast(username, loadGameMessage, true);
            String message = username + "moved from " + decipherPosition(command.getMove().startPosition) + " to " + decipherPosition(command.getMove().endPosition);
            NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
            connections.broadcast(username, notificationMessage, false);
        } catch (InvalidMoveException e) {
            ErrorMessage errorMessage = new ErrorMessage(ERROR, "Error: invalid move. Try highlighting valid moves.");
            connections.rootMessage(username, errorMessage);
        }
    }

    public String decipherPosition(ChessPosition position) {
        String column = switch(position.getRow()) {
            case (1) -> "a";
            case (2) -> "b";
            case (3) -> "c";
            case (4) -> "d";
            case (5) -> "e";
            case (6) -> "f";
            case (7) -> "g";
            case (8) -> "h";
            default -> ".";
        };
        return column + position.getRow();
    }

    public void checkGameStatus(GameData gameData, String username) throws IOException {
        ChessGame game = gameData.game();
        ChessGame.TeamColor otherTeam = (game.currentTeam == WHITE) ? BLACK : WHITE;
        String otherTeamUsername = (otherTeam == WHITE) ? gameData.whiteUsername() : gameData.blackUsername();
        if (game.isInCheck(otherTeam)) {
            sendGameStatusMessage(username, otherTeamUsername + " is in check.");
        }
        if (game.isInCheckmate(otherTeam)) {
            game.endGame();
            sendGameStatusMessage(username, otherTeamUsername + " is in checkmate. Game over. " + username + " wins!");
        }
        if (game.isInStalemate(game.currentTeam)) {
            game.endGame();
            sendGameStatusMessage(username, username + " is in stalemate. Game over. " + otherTeamUsername + " wins!");

        }
        if (game.isInStalemate(otherTeam)) {
            game.endGame();
            sendGameStatusMessage(username, otherTeamUsername + " is in stalemate. Game over. " + username + " wins!");
        }
    }

    public void sendGameStatusMessage(String username, String message) throws IOException {
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(username, notificationMessage, true);
    }

    public void leave(String username, UserGameCommand command) throws IOException {
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
        String message = username + " left the game";
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(username, notificationMessage, false);
        connections.remove(username);
    }

    public void resign(String username, UserGameCommand command) throws IOException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        gameData.game().endGame();
        String winner = (Objects.equals(username, gameData.whiteUsername())) ? gameData.blackUsername() : gameData.whiteUsername();
        String message = username + " resigned." + winner + " wins!";
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(username, notificationMessage, true);
    }

}
