package socialgossip.server.core.entities.message;

import socialgossip.server.core.entities.chat.Chat;
import socialgossip.server.core.entities.user.User;

public class InvalidSenderMessageException extends Exception {
    public InvalidSenderMessageException(final User from, final Chat to) {
        super("can't send message from user " + from.getId() + " to chat " + to.getName());
    }
}
