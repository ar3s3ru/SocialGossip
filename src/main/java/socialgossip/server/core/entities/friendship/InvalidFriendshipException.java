package socialgossip.server.core.entities.friendship;

public class InvalidFriendshipException extends Exception {
    public InvalidFriendshipException(final String message) {
        super("invalid friendship: " + message);
    }
}
