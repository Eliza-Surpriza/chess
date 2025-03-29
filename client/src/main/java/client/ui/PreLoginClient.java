package client.ui;
import client.ServerFacade;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.io.IOException;
import java.util.Arrays;

public class PreLoginClient implements Client {
    private final ServerFacade server;
    private final Repl repl;


    public PreLoginClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String eval(String input) throws IOException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String register(String ... params) throws IOException {
        if (params.length == 3) {
            UserData userData = new UserData(params[0], params[1], params[2]);
            AuthData authData = server.register(userData);
            repl.authToken = authData.authToken();
            repl.isLoggedIn = true;
            return "welcome, " + authData.username() +"! type \"help\" to continue";
        }
        throw new IOException("Expected: register username password email");
    }

    public String login(String ... params) throws IOException {
        if (params.length == 2) {
            LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
            AuthData authData = server.login(loginRequest);
            repl.authToken = authData.authToken();
            repl.isLoggedIn = true;
            return "welcome, " + authData.username() +"! type \"help\" to continue";
        }
        throw new IOException("Expected: login username password");
    }

    public String help() {
        return """
                - "register <username> <password> <email>" (create an account)
                - "login <username> <password>" (sign in)
                - "quit" (exit program)
                - "help" (print this menu again)
                """;
    }
}
