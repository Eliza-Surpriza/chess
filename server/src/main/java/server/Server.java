package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.AlreadyTakenException;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.*;

public class Server {
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    UserService userService = new UserService(userDAO, authDAO);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);


        Spark.exception(ResponseException.class, this::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }


    private Object register(Request req, Response res) throws ResponseException {
        var userData = new Gson().fromJson(req.body(), UserData.class);
        AuthData authData = userService.register(userData);
        return new Gson().toJson(authData);
    }
}
