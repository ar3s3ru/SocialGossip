package socialgossip.server.dataproviders;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.session.AddSessionAccess;
import socialgossip.server.core.gateways.session.GetSessionAccess;
import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.session.SessionNotFoundException;
import socialgossip.server.core.gateways.user.AddUserAccess;
import socialgossip.server.core.gateways.user.GetUserAccess;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserNotFoundException;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class InMemoryRepository
        implements AddUserAccess, GetUserAccess, AddSessionAccess, GetSessionAccess {

    private final HashMap<String, User>    userMap    = new HashMap<>();
    private final HashMap<String, Session> sessionMap = new HashMap<>();

    @Override
    public boolean add(final User user) throws UserAlreadyExistsException {
        synchronized (userMap) {
            Objects.requireNonNull(user);
            Optional.ofNullable(userMap.putIfAbsent(user.getId(), user))
                    .orElseThrow(() -> new UserAlreadyExistsException(user.getId()));
            return true;
        }
    }

    @Override
    public User getByUsername(final String username) throws UserNotFoundException {
        synchronized (userMap) {
            Objects.requireNonNull(username);
            return Optional.ofNullable(userMap.get(username))
                    .orElseThrow(() -> new UserNotFoundException(username));
        }
    }

    @Override
    public void add(final Session session) throws SessionAlreadyExistsException {
        synchronized (sessionMap) {
            Objects.requireNonNull(session);
            Optional.ofNullable(sessionMap.putIfAbsent(session.getToken(), session))
                    .orElseThrow(() -> new SessionAlreadyExistsException(
                            session.getToken(), session.getUser().getId()
                    ));
        }
    }

    @Override
    public Session getByToken(final String token) throws SessionNotFoundException {
        synchronized (sessionMap) {
            Objects.requireNonNull(token);
            return Optional.ofNullable(sessionMap.get(token))
                    .orElseThrow(() -> new SessionNotFoundException(token));
        }
    }
}
