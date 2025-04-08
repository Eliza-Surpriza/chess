package client;


import chess.ChessGame;
import client.ServerFacade;
import client.ui.Repl;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static Repl repl;

    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port + '/';
        serverFacade = new ServerFacade(serverUrl, repl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setUp() throws IOException {
        serverFacade.clear();
        AuthData authData = serverFacade.register(new UserData("kate-the-great", "weather", "weather@machine"));
        CreateResult createResult = serverFacade.createGame(new CreateRequest("duck"), authData.authToken());
        serverFacade.joinGame(new JoinRequest("BLACK", createResult.gameID(), authData.authToken()));
    }

    @Test
    public void register() throws IOException {
        AuthData authData = serverFacade.register(new UserData("reynie", "tamil", "reynard@muldoon"));
        assertEquals("reynie", authData.username());
    }

    @Test
    public void registerAlreadyTaken() {
        UserData userData = new UserData("kate-the-great", "weather", "weather@machine");
        assertThrows(IOException.class, () -> serverFacade.register(userData));
    }

    @Test
    public void login() throws IOException {
        LoginRequest loginRequest = new LoginRequest("kate-the-great", "weather");
        AuthData authData = serverFacade.login(loginRequest);
        assertEquals("kate-the-great", authData.username());
    }

    @Test
    public void loginWrongPassword() {
        LoginRequest loginRequest = new LoginRequest("kate-the-great", "wrong_password");
        assertThrows(IOException.class, () -> serverFacade.login(loginRequest));
    }

    @Test
    public void createGame() throws IOException {
        CreateRequest createRequest = new CreateRequest("penguinWeek");
        LoginRequest loginRequest = new LoginRequest("kate-the-great", "weather");
        AuthData authData = serverFacade.login(loginRequest);
        CreateResult createResult = serverFacade.createGame(createRequest, authData.authToken());
        assertTrue(createResult.gameID() > 0);
    }

    @Test
    public void createGameNoName() throws IOException {
        CreateRequest createRequest = new CreateRequest(null);
        LoginRequest loginRequest = new LoginRequest("kate-the-great", "weather");
        AuthData authData = serverFacade.login(loginRequest);
        assertThrows(IOException.class, () -> serverFacade.createGame(createRequest, authData.authToken()));
    }

    @Test
    public void listGames() throws IOException {
        LoginRequest loginRequest = new LoginRequest("kate-the-great", "weather");
        AuthData authData = serverFacade.login(loginRequest);
        ListResult listResult = serverFacade.listGames(authData.authToken());
        GameData watermelon = new GameData(1, null, "kate-the-great", "duck", new ChessGame());
        Collection<GameData> expected = List.of(watermelon);
        Collection<GameData> actual = listResult.games();
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void listGamesUnauthorized() {
        assertThrows(IOException.class, () -> serverFacade.listGames("unauthorized"));
    }

    @Test
    public void logout() throws IOException {
        LoginRequest loginRequest = new LoginRequest("kate-the-great", "weather");
        AuthData authData = serverFacade.login(loginRequest);
        serverFacade.logout(authData.authToken());
        assertThrows(IOException.class, () -> serverFacade.listGames(authData.authToken()));
    }

    @Test
    public void logoutUnauthorized() {
        assertThrows(IOException.class, () -> serverFacade.logout("unauthorized"));
    }

    @Test
    public void clear() throws IOException {
        serverFacade.clear();
        UserData userData = new UserData("kate-the-great", "weather", "weather@machine");
        AuthData authData = serverFacade.register(userData);
        assertEquals("kate-the-great", authData.username());
    }

    @Test
    public void joinGame() throws IOException {
        AuthData authData = serverFacade.register(new UserData("reynie", "tamil", "reynard@muldoon"));
        serverFacade.joinGame(new JoinRequest("WHITE", 1, authData.authToken()));
        ListResult listResult = serverFacade.listGames(authData.authToken());
        GameData watermelon = new GameData(1, "reynie", "kate-the-great", "duck", new ChessGame());
        Collection<GameData> expected = List.of(watermelon);
        Collection<GameData> actual = listResult.games();
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    public void joinGameAlreadyTaken() throws IOException {
        AuthData authData = serverFacade.register(new UserData("reynie", "tamil", "reynard@muldoon"));
        assertThrows(IOException.class, () -> serverFacade.joinGame(new JoinRequest("BLACK", 1, authData.authToken())));
    }

}
