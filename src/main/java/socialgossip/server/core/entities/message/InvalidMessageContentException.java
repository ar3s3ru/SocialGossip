package socialgossip.server.core.entities.message;

public class InvalidMessageContentException extends Exception {
    public InvalidMessageContentException(final String reason) {
        super("message content is not valid: " + reason);
    }
}
