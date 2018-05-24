package socialgossip.server.core.entities.chat;

import socialgossip.server.core.entities.user.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Conversation extends Group {
    public Conversation(final User user1, final User user2) {
        super(generateConversationName(user1, user2), produceUserSet(user1, user2));
    }

    public final static String generateConversationName(final User user1, final User user2) {
        Objects.requireNonNull(user1);
        Objects.requireNonNull(user2);
        return "chat[" + user1.getId() + ":" + user2.getId() + "]";
    }

    public static Set<User> produceUserSet(final User user1, final User user2) {
        final Set<User> result = new HashSet<>();
        result.add(Objects.requireNonNull(user1));
        result.add(Objects.requireNonNull(user2));
        return result;
    }
}
