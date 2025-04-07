package client.ui;

import chess.ChessGame;
import model.GameData;

import java.io.IOException;
import java.util.Scanner;

public class Repl {
    private Client client;
    public boolean isLoggedIn;
    private final Client preLogin;
    private final Client loggedIn;
    private final Client gamePlay;
    public String authToken;
    public GameData gameData;
    public boolean isInGame;
    public ChessGame.TeamColor color;

    public Repl(String serverUrl) {
        preLogin = new PreLoginClient(serverUrl, this);
        loggedIn = new LoggedInClient(serverUrl, this);
        gamePlay = new GamePlayClient(serverUrl, this);
        client = preLogin;
        isLoggedIn = false;
        authToken = null;
        gameData = null;
        isInGame = false;
        color = null;
    }

    public void run() {
        System.out.println("Welcome to Eliza's Chess Server! :)");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            client = (isLoggedIn) ? (isInGame) ? gamePlay : loggedIn : preLogin;
            System.out.print("\n" + ">>> ");
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (IOException e) {
                var msg = e.getMessage();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

}