package socialgossip.server.core.gateways.user;

import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;

/**
 * Represents a DAO that enables to add new {@link User} objects
 * to a persistence layer (e.g. database, in-memory caches, etc...).
 */
public interface AddUserAccess {
    /**
     * Adds a new {@link User} to the persistence layer.
     * Raises an {@link Exception} if an error occurs.
     * @param user is the {@link User} to add to the persistence layer.
     * @return true if successfully added the new {@link User}, false otherwise.
     * @throws UserAlreadyExistsException if an {@link User} with the same id already exists.
     * @throws GatewayException if an error with the persistence layer occurs.
     */
    boolean add(User user) throws UserAlreadyExistsException, GatewayException;
}
