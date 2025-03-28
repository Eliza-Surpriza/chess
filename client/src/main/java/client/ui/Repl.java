package client.ui;

import java.io.IOException;
import java.util.Scanner;

public class Repl {
    private Client client;
    public boolean isLoggedIn;
    private final Client preLogin;
    private final Client loggedIn;
    public String authToken;

    public Repl(String serverUrl) {
        preLogin = new PreLoginClient(serverUrl, this);
        loggedIn = new LoggedInClient(serverUrl, this);
        client = preLogin;
        isLoggedIn = false;
        authToken = null;
    }

    public void run() {
        System.out.println("Welcome to Eliza's Chess Server! :)");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            client = (isLoggedIn) ? loggedIn : preLogin;
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