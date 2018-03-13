package socialgossip.server.core.entities;

public class InvalidUserIdException extends Exception {
    public InvalidUserIdException(final String message) {
        super("invalid user id specified: " + message);
    }
}
