package socialgossip.server.factories.session;

import socialgossip.server.core.entities.session.InvalidTokenException;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.security.token.TokenGenerator;
import socialgossip.server.security.token.UUIDv4TokenGenerator;
import socialgossip.server.usecases.login.SessionFactory;

import java.util.Objects;

public final class UUIDv4SessionFactory implements SessionFactory {
    private final TokenGenerator tokenGenerator = new UUIDv4TokenGenerator();

    @Override
    public Session produce(final User user) {
        Objects.requireNonNull(user);
        try {
            return new Session(tokenGenerator.generate(), user);
        } catch (InvalidTokenException e) {
            // This should not happen... EVER!
            // But... if it happens, treat it as an unchecked exception that
            // will be caught on the UseCase errors handler.
            throw new RuntimeException(e);
        }
    }
}
