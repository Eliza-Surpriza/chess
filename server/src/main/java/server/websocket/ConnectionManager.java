package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final Gson gson = new Gson();
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, int gameID, Session session) {
        var connection = new Connection(username, gameID, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(String username, ServerMessage serverMessage, boolean everyone) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                sendMessage(username, serverMessage, c, everyone);
            } else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void sendMessage(String username, ServerMessage serverMessage, Connection c, boolean everyone) throws IOException {
        if (everyone || !c.username.equals(username)) {
            if (c.gameID == connections.get(username).gameID) {
                String json = gson.toJson(serverMessage);
                c.send(json);
            }
        }
    }

    public void rootMessage(String username, ServerMessage serverMessage) throws IOException {
        Connection connection = connections.get(username);
        String json = gson.toJson(serverMessage);
        connection.send(json);
    }

}
