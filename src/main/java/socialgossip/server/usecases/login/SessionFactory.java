package socialgossip.server.usecases.login;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;

import java.net.InetAddress;

@FunctionalInterface
public interface SessionFactory {
    Session produce(User user, InetAddress ipAddress);
}
