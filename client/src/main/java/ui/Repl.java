package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private Client client;
    public boolean isLoggedIn;
    private final Client preLogin;
    private final Client loggedIn;

    public Repl(String serverUrl) {
        preLogin = new PreLoginClient(serverUrl, this);
        loggedIn = new LoggedInClient(serverUrl, this);
        client = preLogin;
        isLoggedIn = false;
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
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

//    private void printPrompt() {
//        System.out.print("\n" + ">>> ");
//    }

}