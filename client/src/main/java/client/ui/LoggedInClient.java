package client.ui;

import chess.ChessGame;
import client.ServerFacade;
import client.ServerMessageObserver;
import model.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static client.ui.DrawChessBoard.drawBoard;

public class LoggedInClient implements Client {
    private final ServerFacade server;
    private final Repl repl;
    private HashMap<Integer, GameData> games;

    public LoggedInClient(String serverUrl, Repl repl) throws IOException {
        server = new ServerFacade(serverUrl, repl);
        this.repl = repl;
        games = new HashMap<>();
    }

    public String eval(String input) throws IOException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "create" -> create(params);
            case "list" -> list();
            case "join" -> join(params);
            case "observe" -> observe(params);
            case "logout" -> logout();
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String create(String ... params) throws IOException {
        if (params.length == 1) {
            CreateRequest createRequest = new CreateRequest(params[0]);
            server.createGame(createRequest, repl.authToken);
            return "game " + params[0] + " was created\n";
        }
        throw new IOException("Expected: create gameName");
    }

    public String list() throws IOException {
        ListResult listResult = server.listGames(repl.authToken);
        StringBuilder gameList = new StringBuilder();
        int fakeID = 1;
        for(GameData game : listResult.games()){
            String whitePlayer = (game.whiteUsername() == null) ? "available" : game.whiteUsername();
            String blackPlayer = (game.blackUsername() == null) ? "available" : game.blackUsername();
            gameList.append(fakeID);
            gameList.append(": ").append(game.gameName());
            gameList.append(", white player: ").append(whitePlayer);
            gameList.append(", black player: ").append(blackPlayer).append("\n");
            games.put(fakeID, game);
            fakeID++;
        }
        return gameList.toString();
    }

    public String join(String ... params) throws IOException {
        if (params.length == 2) {
            GameData gameData = getGameFromFakeID(params[0]);
            if (gameData == null) {
                throw new IOException("Game ID not valid. List games to see options.");
            }
            String stringColor;
            if (params[1].equalsIgnoreCase("white")) {
                repl.color = ChessGame.TeamColor.WHITE;
                stringColor = "WHITE";
            } else if (params[1].equalsIgnoreCase("black")) {
                repl.color = ChessGame.TeamColor.BLACK;
                stringColor = "BLACK";
            } else {
                throw new IOException("Expected: join id color");
            }
            server.joinGame(new JoinRequest(stringColor, gameData.gameID(), repl.authToken));
            server.connect(repl.authToken, gameData.gameID());
            repl.gameData = gameData;
            repl.isInGame = true;
            return "\nwelcome to " + gameData.gameName() + "! type help to continue\n";
        }
        throw new IOException("Expected: join id color");
    }

    public String observe(String ... params) throws IOException {
        if (params.length == 1) {
            GameData gameData = getGameFromFakeID(params[0]);
            repl.color = null;
            repl.gameData = gameData;
            repl.isInGame = true;
            server.connect(repl.authToken, gameData.gameID());
            return "observing game " + gameData.gameName() + "\n";
        }
        throw new IOException("Expected: join id color");
    }

    public String logout() throws IOException {
        server.logout(repl.authToken);
        repl.authToken = null;
        repl.isLoggedIn = false;
        return "logged out\n";
    }

    private GameData getGameFromFakeID(String fakeID) throws IOException {
        int id;
        try {
            id = Integer.parseInt(fakeID);
        }
        catch (NumberFormatException e) {
            throw new IOException("Expected: join id color");
        }
        return games.get(id);
    }

    public String help() {
        return """
                - "create <gameName>" (create a chess game)
                - "list" (list all games)
                - "join <game ID> <black or white>" (join game - specify ID and color)
                - "observe <game ID>" (observe game - specify ID)
                - "logout" (logout and return to login menu)
                - "quit" (exit program)
                - "help" (print this menu again)
                """;
    }


}
