package socialgossip.server.usecases.login;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;

@FunctionalInterface
public interface SessionFactory {
    Session produce(User user);
}
