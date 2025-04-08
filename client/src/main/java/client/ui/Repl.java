package client.ui;

import chess.ChessGame;
import client.ServerMessageObserver;
import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static client.ui.DrawChessBoard.drawBoard;

public class Repl implements ServerMessageObserver {
    private Client client;
    public boolean isLoggedIn;
    private final Client preLogin;
    private final Client loggedIn;
    private final Client gamePlay;
    public String authToken;
    public GameData gameData;
    public boolean isInGame;
    public ChessGame.TeamColor color;
    public ChessGame game;

    public Repl(String serverUrl) throws IOException {
        preLogin = new PreLoginClient(serverUrl, this);
        loggedIn = new LoggedInClient(serverUrl, this);
        gamePlay = new GamePlayClient(serverUrl, this);
        client = preLogin;
        isLoggedIn = false;
        authToken = null;
        gameData = null;
        game = null;
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

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }

    private void displayNotification(String message) {
        System.out.print(message);
        System.out.print("\n" + ">>> ");
    }

    private void displayError(String error) {
        System.out.print(error);
        System.out.print("\n" + ">>> ");
    }

    private void loadGame(ChessGame gameToDraw) {
        boolean upsideDown = (Objects.equals(color, ChessGame.TeamColor.BLACK));
        drawBoard(gameToDraw.gameBoard, upsideDown, null, null);
        System.out.print("\n" + ">>> ");
        game = gameToDraw;
    }

}