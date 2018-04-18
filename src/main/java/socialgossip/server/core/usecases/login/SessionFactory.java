package socialgossip.server.core.usecases.login;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;

import java.time.temporal.TemporalUnit;

public interface SessionFactory {
    Session produce(User user);
    Session produceWithExpire(User user, long expireDelta, TemporalUnit expireUnit);
}
