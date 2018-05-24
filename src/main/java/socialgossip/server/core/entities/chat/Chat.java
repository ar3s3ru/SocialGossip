package socialgossip.server.core.entities.chat;

import socialgossip.server.core.entities.user.User;

import java.util.Set;

public interface Chat {
    String getName();

    Set<User> getPartecipants();

    default boolean canAcceptMessagesFrom(final User user) {
        return getPartecipants().contains(user);
    }
}
