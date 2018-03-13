package socialgossip.server.core.entities;

public class EmptyPasswordException extends Exception {
    public EmptyPasswordException(final String message) {
        super("empty password specified: " + message);
    }
}
