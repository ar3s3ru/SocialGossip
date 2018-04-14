package socialgossip.server.core.usecases;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractUseCase<I, O, E extends ErrorsHandler>
        implements UseCase<I, O, E> {

    protected abstract void onExecute(I input, Consumer<O> onSuccess, E errors);

    public final void execute(final I input, final Consumer<O> onSuccess, final E errors) {
        this.onExecute(Objects.requireNonNull(input),
                       Objects.requireNonNull(onSuccess),
                       Objects.requireNonNull(errors));
    }
}
