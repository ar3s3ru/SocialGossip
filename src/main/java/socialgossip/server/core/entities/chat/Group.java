package socialgossip.server.core.entities.chat;

import socialgossip.server.core.entities.user.User;

import java.util.Objects;
import java.util.Set;

public class Group implements Chat {
    private final String    name;
    private final Set<User> partecipants;

    public Group(final String name, final Set<User> partecipants) {
        this.name         = Objects.requireNonNull(name);
        this.partecipants = Objects.requireNonNull(partecipants);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<User> getPartecipants() {
        return partecipants;
    }
}
