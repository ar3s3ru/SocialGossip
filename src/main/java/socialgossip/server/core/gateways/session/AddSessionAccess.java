package socialgossip.server.core.gateways.session;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;

/**
 * Represents a DAO that enables to add new {@link Session} objects to the
 * persistence storage layer.
 */
public interface AddSessionAccess {
    /**
     * Adds a new {@link Session} to the persistence storage.
     *
     * When added, all other {@link Session}s of the same
     * {@link socialgossip.server.core.entities.user.User} will be removed from the
     * persistence storage.
     *
     * @param session is the {@link Session} to add.
     * @throws SessionAlreadyExistsException if the {@link Session} token already exists.
     * @throws GatewayException if there's been an external error with the persistence layer,
     *         typically connection errors, etc.
     */
    void add(Session session) throws SessionAlreadyExistsException, GatewayException;
}
