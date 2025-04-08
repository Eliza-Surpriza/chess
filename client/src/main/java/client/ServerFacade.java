package client;

import chess.ChessMove;
import com.google.gson.Gson;
import model.*;

import java.io.IOException;

public class ServerFacade {
    private final HttpCommunicator communicator;
    private final WebsocketCommunicator websocketCommunicator;
    private final Gson gson = new Gson();

    public ServerFacade(String serverUrl, ServerMessageObserver serverMessageObserver) throws IOException {
        this.communicator = new HttpCommunicator(serverUrl);
        this.websocketCommunicator = new WebsocketCommunicator(serverUrl, serverMessageObserver);
    }

    public AuthData register(UserData userData) throws IOException {
        String requestBody = gson.toJson(userData);
        String json =  communicator.doPost("/user", requestBody, null);
        return gson.fromJson(json, AuthData.class);

    }

    public AuthData login(LoginRequest loginRequest) throws IOException {
        String requestBody = gson.toJson(loginRequest);
        String json = communicator.doPost("/session", requestBody, null);
        return gson.fromJson(json, AuthData.class);
    }

    public CreateResult createGame(CreateRequest createRequest, String authToken) throws IOException {
        String requestBody = gson.toJson(createRequest);
        String json =  communicator.doPost("/game", requestBody, authToken);
        return gson.fromJson(json, CreateResult.class);
    }

    public ListResult listGames(String authToken) throws IOException {
        String json = communicator.doGet("/game", authToken);
        return gson.fromJson(json, ListResult.class);
    }

    public String logout(String authToken) throws IOException {
        return communicator.doDelete("/session", authToken);
    }

    public void clear() throws IOException {
        communicator.doDelete("/db", null);
    }

    public String joinGame(JoinRequest joinRequest) throws IOException {
        String requestBody = gson.toJson(joinRequest);
        return communicator.doPut("/game", requestBody, joinRequest.authToken());
    }

    public void connect(String authToken, int gameID) throws IOException {
        websocketCommunicator.connect(authToken, gameID);
    }

    public void resign(String authToken, int gameID) throws IOException {
        websocketCommunicator.resign(authToken, gameID);
    }

    public void leave(String authToken, int gameID) throws IOException {
        websocketCommunicator.leave(authToken, gameID);
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        websocketCommunicator.makeMove(authToken, gameID, move);
    }
}
