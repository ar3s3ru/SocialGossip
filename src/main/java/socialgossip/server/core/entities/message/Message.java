package socialgossip.server.core.entities.message;

import socialgossip.server.core.entities.chat.Chat;
import socialgossip.server.core.entities.user.User;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Message {
    public  final User    sentFrom;
    public  final Chat    sentTo;
    public  final Date    sentDate;
    private final String  id;
    private final String  content;
    private final boolean read;

    private static void checkMessageAllowance(final User sender, final Chat receiver)
            throws InvalidSenderMessageException {
        if (!receiver.canAcceptMessagesFrom(sender)) {
            throw new InvalidSenderMessageException(sender, receiver);
        }
    }

    private static String checkMessageContent(final String content)
            throws InvalidMessageContentException {
        if (Objects.isNull(content)) {
            throw new InvalidMessageContentException("null content");
        }
        if (content.length() == 0) {
            throw new InvalidMessageContentException("content can't be empty");
        }
        return content;
    }

    private static String checkMessageId(final String id)
            throws InvalidMessageIDException {
        if (Objects.isNull(id)) {
            throw new InvalidMessageIDException("null id");
        }
        if (id.length() == 0) {
            throw new InvalidMessageIDException("id can't be empty");
        }
        return id;
    }

    public Message(final String id, final String content, final User from, final Chat to)
            throws InvalidSenderMessageException, InvalidMessageIDException, InvalidMessageContentException {
        this(id, content, false, from, to);
    }

    public Message(final String id, final String content, final boolean read,
                   final User from, final Chat to)
            throws InvalidSenderMessageException, InvalidMessageIDException, InvalidMessageContentException {
        this(id, content, read, from, to, Date.from(Instant.now()));
    }

    public Message(final String id, final String content, final boolean read,
                   final User from, final Chat to, final Date sentDate)
            throws InvalidSenderMessageException, InvalidMessageIDException, InvalidMessageContentException {
        this.sentFrom = Objects.requireNonNull(from);
        this.sentTo   = Objects.requireNonNull(to);
        checkMessageAllowance(from, to);
        this.sentDate = Objects.requireNonNull(sentDate);
        this.content  = checkMessageContent(content);
        this.read     = read;
        this.id       = checkMessageId(id);
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return read;
    }
}
