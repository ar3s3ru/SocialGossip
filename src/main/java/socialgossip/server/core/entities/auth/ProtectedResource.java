package socialgossip.server.core.entities.auth;

public interface ProtectedResource {
    void checkAllowance(Permission permission) throws UnauthorizedException;
}
