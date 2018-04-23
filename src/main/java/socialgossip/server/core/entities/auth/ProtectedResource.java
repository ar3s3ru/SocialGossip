package socialgossip.server.core.entities.auth;

/**
 * Represents a protected resource, which needs a {@link Permission} in order
 * to be accessed, or used.
 */
public interface ProtectedResource {
    /**
     * Checks if a given {@link Permission} is allowed to access this protected resource.
     * @param permission is the {@link Permission} we want to use to access the resource.
     * @throws UnauthorizedException if the {@link Permission} is not valid to
     *         access the resource.
     */
    void checkAllowance(Permission permission) throws UnauthorizedException;
}
