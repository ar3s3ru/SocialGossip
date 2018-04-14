package socialgossip.server.core.usecases;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface UseCase<Input, Output> {
    void execute(Input input, Consumer<Output> onSuccess, Consumer<Throwable> onError);
}