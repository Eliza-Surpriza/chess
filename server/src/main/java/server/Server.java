package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    AuthDAO authDAO = new MemoryAuthDAO();
    UserDAO userDAO = new MemoryUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    UserService userService = new UserService(userDAO, authDAO);
    GameService gameService = new GameService(gameDAO, authDAO);
    AuthService authService = new AuthService(authDAO);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::create);
        Spark.put("/game", this::join);
        Spark.get("/game", this::list);
        Spark.delete("/db", this::clear);

        Spark.exception(ResponseException.class, this::exceptionHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.getStatusCode());
        res.body(ex.toJson());
    }


    private Object register(Request req, Response res) throws ResponseException, DataAccessException {
        var userData = new Gson().fromJson(req.body(), UserData.class);
        AuthData authData = userService.register(userData);
        return new Gson().toJson(authData);
    }

    private Object login(Request req, Response res) throws ResponseException {
        var loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        AuthData authData = userService.login(loginRequest);
        return new Gson().toJson(authData);
    }

    private Object logout(Request req, Response res) throws ResponseException {
        var authToken = req.headers("authorization");
        authService.authorize(authToken);
        userService.logout(authToken);
        return "";
    }

    private Object create(Request req, Response res) throws ResponseException {
        var authToken = req.headers("authorization");
        authService.authorize(authToken);
        var createRequest = new Gson().fromJson(req.body(), CreateRequest.class);
        CreateResult createResult = gameService.createGame(createRequest);
        return new Gson().toJson(createResult);
    }

    private Object join(Request req, Response res) throws ResponseException {
        var authToken = req.headers("authorization");
        authService.authorize(authToken);
        var partialJoinRequest = new Gson().fromJson(req.body(), JoinRequest.class);
        JoinRequest joinRequest = new JoinRequest(partialJoinRequest.playerColor(), partialJoinRequest.gameID(), authToken);
        gameService.joinGame(joinRequest);
        return "";
    }

    private Object list(Request req, Response res) throws ResponseException {
        var authToken = req.headers("authorization");
        authService.authorize(authToken);
        ListResult listResult = gameService.listGames();
        return new Gson().toJson(listResult);
    }

    private Object clear(Request req, Response res) throws ResponseException {
        userService.clear();
        gameService.clear();
        return "";
    }
}
