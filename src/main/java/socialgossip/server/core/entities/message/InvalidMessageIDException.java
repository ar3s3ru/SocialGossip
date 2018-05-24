package socialgossip.server.core.entities.message;

public class InvalidMessageIDException extends Exception {
    public InvalidMessageIDException(final String reason) {
        super("message id is not valid: " + reason);
    }
}
