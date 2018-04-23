package socialgossip.server.core.entities.session;

/**
 * Represents an object that has a scope tied to the {@link Session} that carries along.
 *
 * An example would be a {@link socialgossip.server.core.gateways.notifications.Notification},
 * in which case the {@link SessionScoped} object represents the {@link Session} that
 * generated the {@link socialgossip.server.core.gateways.notifications.Notification} itself.
 */
public interface SessionScoped {
    Session from();
}
