package exception;

public class DataAccessException extends ResponseException {
    public DataAccessException(String message) {
        super(500, message);
    }
}
