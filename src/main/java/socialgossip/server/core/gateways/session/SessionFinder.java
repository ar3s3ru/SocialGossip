package socialgossip.server.core.gateways.session;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;

/**
 * Represents a DAO that gives access to {@link Session} objects retrieval
 * by providing an identification, such as a token.
 */
public interface SessionFinder {
    /**
     * Returns the {@link Session} object with the provided {@code token}.
     * @param token is the {@link Session} token to retrieve.
     * @return the {@link Session} object with the specified {@code token}.
     * @throws SessionNotFoundException if no {@link Session} with specified {@code token}
     *         has been found.
     * @throws GatewayException if there's been an external error with the persistence layer.
     */
    Session findByToken(String token) throws SessionNotFoundException, GatewayException;
}
