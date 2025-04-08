package client;

import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.commands.*;

import javax.websocket.*;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator extends Endpoint {
    Session session;
    ServerMessageObserver serverMessageObserver;
    private final Gson gson = new Gson();

    public WebsocketCommunicator(String url, ServerMessageObserver serverMessageObserver) throws IOException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageObserver = serverMessageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler((MessageHandler.Whole<String>) this::onMessage);

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void onMessage(String messageString) {
        try {
            ServerMessage message = gson.fromJson(messageString, ServerMessage.class);
            serverMessageObserver.notify(message);
        } catch (Exception ex) {
            serverMessageObserver.notify(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage()));
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        try {
            var command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(gson.toJson(command));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void connect(String authToken, int gameID) throws IOException {
        sendUserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
    }

    public void resign(String authToken, int gameID) throws IOException {
        sendUserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
    }

    public void leave(String authToken, int gameID) throws IOException {
        sendUserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        try {
            this.session.close();
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private void sendUserGameCommand(UserGameCommand.CommandType commandType, String authToken, int gameID) throws IOException {
        try {
            var command = new UserGameCommand(commandType, authToken, gameID);
            this.session.getBasicRemote().sendText(gson.toJson(command));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // leaving this here to appease endpoint, but I don't need it apparently
    }
}
