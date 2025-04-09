package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
import websocket.messages.*;

import static websocket.messages.ServerMessage.ServerMessageType.*;

import java.io.IOException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

@WebSocket
public class WebSocketHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final Gson gson;
    private final ConnectionManager connections = new ConnectionManager();


    public WebSocketHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.gson = new Gson();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            String username = authDAO.getAuth(command.getAuthToken()).username();
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(username, gson.fromJson(message, MakeMoveCommand.class));
                case LEAVE -> leave(username, command);
                case RESIGN -> resign(username, command);
            }
        } catch (UnauthorizedException ex) {
            ErrorMessage errorMessage = new ErrorMessage(ERROR, "Error: Unauthorized");
            String json = gson.toJson(errorMessage);
            session.getRemote().sendString(json);
        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage(ERROR, ex.getMessage());
            String json = gson.toJson(errorMessage);
            session.getRemote().sendString(json);
        }
    }


    public void connect(Session session, String username, UserGameCommand command) throws IOException {
        connections.add(username, command.getGameID(), session);
        GameData gameData = gameDAO.getGame(command.getGameID());
        String color;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            color = "white";
        } else if (Objects.equals(gameData.blackUsername(), username)) {
            color = "black";
        } else {
            color = "observer";
        }
        String message = username + " joined game as " + color;
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(username, notificationMessage, false);
        LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, gameData);
        connections.rootMessage(username, loadGameMessage);
    }

    public void makeMove(String username, MakeMoveCommand command) throws IOException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessMove move = command.getMove();
        try {
            checkIfShouldMove(gameData, move, username);
            gameData.game().makeMove(move);
            ChessGame game = checkGameStatus(gameData, username);
            String white = gameData.whiteUsername();
            String black = gameData.blackUsername();
            GameData updated = new GameData(gameData.gameID(), white, black, gameData.gameName(), game);
            gameDAO.updateGame(updated);
            LoadGameMessage loadGameMessage = new LoadGameMessage(LOAD_GAME, updated);
            connections.broadcast(username, loadGameMessage, true);
            String message = username + " moved from " + decipher(move.startPosition) + " to " + decipher(move.endPosition);
            NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
            connections.broadcast(username, notificationMessage, false);
        } catch (InvalidMoveException e) {
            ErrorMessage errorMessage = new ErrorMessage(ERROR, e.getMessage());
            connections.rootMessage(username, errorMessage);
        }
    }

    public void checkIfShouldMove(GameData gameData, ChessMove move, String username) throws InvalidMoveException {
        if (gameData.game().currentTeam == BLACK && !Objects.equals(gameData.blackUsername(), username)) {
            throw new InvalidMoveException("Error: Wait for your turn");
        }
        if (gameData.game().currentTeam == WHITE && !Objects.equals(gameData.whiteUsername(), username)) {
            throw new InvalidMoveException("Error: Wait for your turn");
        }
        if (gameData.game().currentTeam != gameData.game().gameBoard.getPiece(move.startPosition).getTeamColor()) {
            throw new InvalidMoveException("Error: You can't move your opponent's piece");
        }
        if (!Objects.equals(username, gameData.whiteUsername()) && !Objects.equals(username, gameData.blackUsername())) {
            throw new InvalidMoveException("Error: Observers cannot move");
        }
        if (gameData.game().gameOver) {
            throw new InvalidMoveException("Error: Game over");
        }
    }

    public String decipher(ChessPosition position) {
        String column = switch (position.getColumn()) {
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

    public ChessGame checkGameStatus(GameData gameData, String username) throws IOException {
        ChessGame game = gameData.game();
        ChessGame.TeamColor otherTeam = game.currentTeam;
        String otherTeamUsername = (otherTeam == WHITE) ? gameData.whiteUsername() : gameData.blackUsername();
        if (game.isInCheckmate(otherTeam)) {
            game.endGame();
            sendGameStatusMessage(username, otherTeamUsername + " is in checkmate. Game over. " + username + " wins!");
        } else if (game.isInCheck(otherTeam)) {
            sendGameStatusMessage(username, otherTeamUsername + " is in check.");
        } else if (game.isInStalemate(otherTeam)) {
            game.endGame();
            sendGameStatusMessage(username, otherTeamUsername + " is in stalemate. Game over. " + username + " wins!");
        }
        return game;
    }

    public void sendGameStatusMessage(String username, String message) throws IOException {
        NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(username, notificationMessage, true);
    }

    public void leave(String username, UserGameCommand command) throws IOException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        GameData updated;
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        if (Objects.equals(white, username)) {
            updated = new GameData(gameData.gameID(), null, black, gameData.gameName(), gameData.game());
        } else if (Objects.equals(black, username)) {
            updated = new GameData(gameData.gameID(), white, null, gameData.gameName(), gameData.game());
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
        ChessGame game = gameData.game();
        if (!Objects.equals(username, gameData.whiteUsername()) && !Objects.equals(username, gameData.blackUsername())) {
            connections.rootMessage(username, new ErrorMessage(ERROR, "Error: observers cannot resign"));
        } else if (game.gameOver) {
            connections.rootMessage(username, new ErrorMessage(ERROR, "Error: game over"));
        } else {
            game.endGame();
            String white = gameData.whiteUsername();
            String black = gameData.blackUsername();
            GameData updated = new GameData(gameData.gameID(), white, black, gameData.gameName(), game);
            gameDAO.updateGame(updated);
            String winner = (Objects.equals(username, gameData.whiteUsername())) ? gameData.blackUsername() : gameData.whiteUsername();
            String message = username + " resigned." + winner + " wins!";
            NotificationMessage notificationMessage = new NotificationMessage(NOTIFICATION, message);
            connections.broadcast(username, notificationMessage, true);
        }
    }

}
