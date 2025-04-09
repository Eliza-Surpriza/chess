package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String username;
    public Session session;
    public int gameID;

    public Connection(String visitorName, int gameID, Session session) {
        this.username = visitorName;
        this.gameID = gameID;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}