package socialgossip.server.dataproviders;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.session.SessionInserter;
import socialgossip.server.core.gateways.session.SessionNotFoundException;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.core.gateways.user.UserInserter;
import socialgossip.server.core.gateways.user.UserNotFoundException;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class InMemoryRepository
        implements UserInserter, UserFinder, SessionInserter, SessionFinder {

    private final HashMap<String, User>    userMap;
    private final HashMap<String, Session> sessionMap;

    public InMemoryRepository() {
        this(null, null);
    }

    InMemoryRepository(final HashMap<String, User> userMap,
                       final HashMap<String, Session> sessionMap) {
        this.userMap    = Optional.ofNullable(userMap).orElseGet(HashMap::new);
        this.sessionMap = Optional.ofNullable(sessionMap).orElseGet(HashMap::new);
    }

    @Override
    public void insert(final User user) throws UserAlreadyExistsException {
        synchronized (userMap) {
            Objects.requireNonNull(user);
            if (Objects.nonNull(userMap.putIfAbsent(user.getId(), user))){
                throw new UserAlreadyExistsException(user.getId());
            }
        }
    }

    @Override
    public User findByUsername(final String username) throws UserNotFoundException {
        synchronized (userMap) {
            Objects.requireNonNull(username);
            return Optional.ofNullable(userMap.get(username))
                    .orElseThrow(() -> new UserNotFoundException(username));
        }
    }

    @Override
    public void insert(final Session session) throws SessionAlreadyExistsException {
        synchronized (sessionMap) {
            Objects.requireNonNull(session);
            if (Objects.nonNull(sessionMap.putIfAbsent(session.getToken(), session))) {
                throw new SessionAlreadyExistsException(
                        session.getToken(), session.getUser().getId()
                );
            }
        }
    }

    @Override
    public Session findByToken(final String token) throws SessionNotFoundException {
        synchronized (sessionMap) {
            Objects.requireNonNull(token);
            return Optional.ofNullable(sessionMap.get(token))
                    .orElseThrow(() -> new SessionNotFoundException(token));
        }
    }
}
