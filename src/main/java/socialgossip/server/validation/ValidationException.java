package socialgossip.server.entrypoints.validation;

public class ValidationException extends Exception {
    public ValidationException(final String field, final String reason) {
        super("Invalid field \"" + field + "\": " + reason);
    }
}
