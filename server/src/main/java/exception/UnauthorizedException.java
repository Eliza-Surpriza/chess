package exception;

public class UnauthorizedException extends ResponseException {
    public UnauthorizedException(String message) {
        super(401, message);
    }
}
