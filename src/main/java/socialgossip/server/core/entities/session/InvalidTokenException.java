package socialgossip.server.core.entities.session;

/**
 * Exception raised when an invalid token has been used while trying
 * to create a new {@link Session} instance.
 */
public class InvalidTokenException extends Exception {
    /**
     * Creates a new {@link InvalidTokenException} that provides informations on
     * why a {@link Session} token has been marked as 'invalid'.
     * @param token is the invalid {@link Session} token.
     * @param reason is the reason why the token is invalid.
     */
    public InvalidTokenException(final String token, final String reason) {
        super("Invalid session token \"" + token + "\": " + reason);
    }
}
