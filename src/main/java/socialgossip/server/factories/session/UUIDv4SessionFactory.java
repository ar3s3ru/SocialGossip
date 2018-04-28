package socialgossip.server.factories.session;

import socialgossip.server.core.entities.session.InvalidTokenException;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.security.token.TokenGenerator;
import socialgossip.server.security.token.UUIDv4TokenGenerator;
import socialgossip.server.usecases.login.SessionFactory;

import java.net.InetAddress;
import java.util.Objects;

public final class UUIDv4SessionFactory implements SessionFactory {
    private final TokenGenerator tokenGenerator = new UUIDv4TokenGenerator();

    @Override
    public Session produce(final User user, final InetAddress ipAddress) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(ipAddress);
        try {
            return new Session(tokenGenerator.generate(), user, ipAddress);
        } catch (InvalidTokenException e) {
            // This should not happen... EVER!
            // But... if it happens, treat it as an unchecked exception that
            // will be caught on the UseCase errors handler.
            throw new RuntimeException(e);
        }
    }
}
