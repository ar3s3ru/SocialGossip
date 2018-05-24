package socialgossip.server.core.entities.friendship;

import socialgossip.server.core.entities.user.User;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public boolean involves(final User user1, final User user2) {
        return Arrays.stream(subjects)
                    .filter(u -> u.equals(user1) || u.equals(user2))
                    .collect(Collectors.toSet()).size() == 2;
    }
}
