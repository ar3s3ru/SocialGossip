package socialgossip.server.core.usecases;

import java.util.function.Consumer;

@FunctionalInterface
public interface UseCase<I, O, E extends ErrorsHandler> {
    void execute(I input, Consumer<O> onSuccess, E errors);
}