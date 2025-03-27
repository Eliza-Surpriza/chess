import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(0);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setUp() throws IOException {
        serverFacade.clear();
        // register someone
        // create game
    }

    @Test
    public void Register() {
        // try registering
        // see if it returns authdata
    }

    @Test
    public void RegisterAlreadyTaken() {
        // try registering with repeat info
        // see if it throws IOException
    }

    @Test
    public void Login() {
        // try login someone
        // see if it gives an authtoken (check if it is longish?)
    }

    @Test
    public void LoginWrongPassword() {
        // try login someone wrong password
        // see if error
    }

    @Test
    public void CreateGame() {
        // try create game
        // see if returns game data json
    }

    @Test
    public void CreateGameNoName() {
        // try create game with no name
        // see if error
    }

    @Test
    public void ListGames() {
        // try list games
        // see if is list of game:)
    }

}
