package client.ui;

import client.ServerFacade;
import model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static client.ui.DrawChessBoard.drawBoard;

public class LoggedInClient implements Client {
    private final ServerFacade server;
    private final Repl repl;
    private HashMap<Integer, GameData> games;

    public LoggedInClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
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
            case "logout" -> logout(params);
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
            gameList.append(fakeID);
            gameList.append(": ").append(game.gameName());
            gameList.append(", white player: ").append(game.whiteUsername());
            gameList.append(", black player: ").append(game.blackUsername()).append("\n");
            games.put(fakeID, game);
            fakeID++;
        }
        return gameList.toString();
    }

    public String join(String ... params) throws IOException {
        if (params.length == 2) {
            GameData gameData = getGameFromFakeID(params[0]);
            String color;
            if (params[1].equalsIgnoreCase("white")) {
                color = "WHITE";
                drawBoard(gameData.game().getBoard(), false);
            } else if (params[1].equalsIgnoreCase("black")) {
                color = "BLACK";
                drawBoard(gameData.game().getBoard(), true);
            } else {
                throw new IOException("Expected: join id color");
            }
            server.joinGame(new JoinRequest(color, gameData.gameID(), repl.authToken));
            return "joined game " + gameData.gameName() + "\n";
        }
        throw new IOException("Expected: join id color");
    }

    public String observe(String ... params) throws IOException {
        if (params.length == 1) {
            GameData gameData = getGameFromFakeID(params[0]);
            drawBoard(gameData.game().getBoard(), false);
            return "observing game " + gameData.gameName() + "\n";
        }
        throw new IOException("Expected: join id color");
    }

    public String logout(String ... params) throws IOException {
        server.logout(repl.authToken);
        repl.authToken = null;
        repl.isLoggedIn = false;
        return "logged out\n";
    }

    private GameData getGameFromFakeID(String fakeID) throws IOException {
        int ID;
        try {
            ID = Integer.parseInt(fakeID);
        }
        catch (NumberFormatException e) {
            throw new IOException("Expected: join id color");
        }
        return games.get(ID);
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
