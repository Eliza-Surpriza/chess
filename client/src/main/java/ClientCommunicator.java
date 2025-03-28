import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClientCommunicator {
    private final String serverUrl;

    public ClientCommunicator(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String doPost(String endpoint, String jsonBody, String authToken) throws IOException {
        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            InputStream errorStream = connection.getErrorStream();
            String errorMessage = errorStream != null
                    ? new String(errorStream.readAllBytes(), StandardCharsets.UTF_8)
                    : "Unknown error occurred.";

            throw new IOException("Server error: " + errorMessage);
        }
    }


    public String doGet(String endpoint, String authToken) throws IOException {
        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            InputStream errorStream = connection.getErrorStream();
            String errorMessage = errorStream != null
                    ? new String(errorStream.readAllBytes(), StandardCharsets.UTF_8)
                    : "Unknown error occurred.";

            throw new IOException("Server error: " + errorMessage);
        }
    }

    public String doDelete(String endpoint, String authToken) throws IOException {
        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return "Success";
        } else {
            InputStream errorStream = connection.getErrorStream();
            String errorMessage = errorStream != null
                    ? new String(errorStream.readAllBytes(), StandardCharsets.UTF_8)
                    : "Unknown error occurred.";

            throw new IOException("Server error: " + errorMessage);
        }
    }

    public String doPut(String endpoint, String jsonBody, String authToken) throws IOException {
        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setRequestProperty("Authorization", authToken);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return "Success";
        } else {
            InputStream errorStream = connection.getErrorStream();
            String errorMessage = errorStream != null
                    ? new String(errorStream.readAllBytes(), StandardCharsets.UTF_8)
                    : "Unknown error occurred.";

            throw new IOException("Server error: " + errorMessage);
        }
    }

}
