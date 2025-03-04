package server;

import com.google.gson.Gson;
import model.UserData;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (request, response) -> {
            // Create something
            // okay so the request is in json and I need to turn it into a UserData
            // but if I run into a problem, I'll send a BadRequest exception
            // then I will call the register method
            // and I will get some authData and turn it into a json object
            // then return that!
            var serializer = new Gson();
            UserData userData = serializer.fromJson(request, UserData.class);

        });


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
