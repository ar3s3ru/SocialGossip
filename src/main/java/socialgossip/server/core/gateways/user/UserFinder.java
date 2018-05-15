package socialgossip.server.core.gateways.user;

import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;

/**
 * Represents a DAO that gives ability to retrieve {@link User} objects by
 * a number of paramenters (username, etc.)
 */
public interface UserFinder {
    /**
     * Gets an {@link User} by its {@code username}, if exists.
     * @param username is the {@link User} name to retrieve.
     * @return an {@link User} that has the specified {@code username}.
     * @throws UserNotFoundException if no {@link User} with that {@code username} has been found.
     * @throws GatewayException if an external error with the persistence layer occurred.
     */
    User findByUsername(String username) throws UserNotFoundException, GatewayException;
}
