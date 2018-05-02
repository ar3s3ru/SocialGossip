package socialgossip.server.core.entities.friendship;

import socialgossip.server.core.entities.user.User;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Friendship {
    private final User[] subjects;
    private final Date   issueDate;

    public Friendship(final User from, final User to) throws InvalidFriendshipException {
        this(from, to, Date.from(Instant.now()));
    }

    public Friendship(final User from, final User to, final Date issueDate)
            throws InvalidFriendshipException {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (from.equals(to)) {
            throw new InvalidFriendshipException("can't send friendship to oneself");
        }
        this.issueDate = Objects.requireNonNull(issueDate);
        subjects = new User[]{from, to};
    }

    public User[] getSubjects() {
        return subjects;
    }

    public Date getIssueDate() {
        return issueDate;
    }
}
