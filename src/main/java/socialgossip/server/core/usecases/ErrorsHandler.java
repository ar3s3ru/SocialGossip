package socialgossip.server.core.usecases;

/**
 * Represents an handler, or callback, that handles error cases occurred
 * when executing an {@link UseCase} logic.
 */
@FunctionalInterface
public interface ErrorsHandler {
    void onError(Exception e);
}
