import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.*;

import java.io.IOException;

public class ServerFacade {
    private final ClientCommunicator communicator;
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        String serverUrl = "http://localhost:" + port + '/';
        this.communicator = new ClientCommunicator(serverUrl);
    }

    public AuthData registerUser(UserData userData) throws IOException {
        String requestBody = gson.toJson(userData);
        String json =  communicator.doPost("/user", requestBody, null);
        return gson.fromJson(json, AuthData.class);

    }

    public AuthData login(LoginRequest loginRequest) throws IOException {
        String requestBody = gson.toJson(loginRequest);
        String json = communicator.doPost("/session", requestBody, null);
        return gson.fromJson(json, AuthData.class);
    }

    public String createGame(CreateRequest createRequest, String authToken) throws IOException {
        String requestBody = gson.toJson(createRequest);
        return communicator.doPost("/game", requestBody, authToken);
    }

    public String listGames(String authToken) throws IOException {
        return communicator.doGet("/game", authToken);
    }

    public String logout(String authToken) throws IOException {
        return communicator.doDelete("/session", authToken);
    }

    public String clear() throws IOException {
        return communicator.doDelete("/db", null);
    }

    private String joinGame(JoinRequest joinRequest) throws IOException {
        String requestBody = gson.toJson(joinRequest);
        return communicator.doPut("/game", requestBody, joinRequest.authToken());
    }
}
