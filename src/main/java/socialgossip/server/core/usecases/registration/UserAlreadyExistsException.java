package socialgossip.server.core.usecases.registration;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(final String id) {
        super("user " + id + " already exists");
    }
}
