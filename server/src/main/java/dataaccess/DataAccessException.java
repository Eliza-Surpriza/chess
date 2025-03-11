package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    public String message;

    public DataAccessException(String message) {
        super(message);
    }
}
