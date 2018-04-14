package socialgossip.server.core.usecases.registration;

public class EmptyPasswordException extends Exception {
    public EmptyPasswordException(final String message) {
        super("empty password specified: " + message);
    }
}
