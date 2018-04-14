package socialgossip.server.core.usecases;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractUseCase<Input, Output>
        implements UseCase<Input, Output> {

    protected abstract void onExecute(Input input, Consumer<Output> onSuccess, Consumer<Throwable> onError);

    public final void execute(final Input input, final Consumer<Output> onSuccess, final Consumer<Throwable> onError) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(onSuccess);
        Objects.requireNonNull(onError);
        onExecute(input, onSuccess, onError);
    }
}
