package socialgossip.server.usecases;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Abstract class that allows safe implementations of an {@link UseCase}.
 * @param <I> is the {@link UseCase} input type.
 * @param <O> is the {@link UseCase} produced result type.
 */
public abstract class AbstractUseCase<I extends UseCase.Input, O> implements UseCase<I, O> {
    /**
     * Executes the {@link UseCase} logic, and guarantees that input,
     * success callback and errors handler are valid and not-null.
     * @param input is the not-null {@link UseCase} input.
     * @param onSuccess is the not-null {@link UseCase} success callback.
     * @param onError is the not-null {@link UseCase} errors callback.
     */
    protected abstract void onExecute(I input, Consumer<O> onSuccess, Consumer<Throwable> onError);

    public final void execute(final I input, final Consumer<O> onSuccess, final Consumer<Throwable> onError) {
        this.onExecute(Objects.requireNonNull(input),
                       Objects.requireNonNull(onSuccess),
                       Objects.requireNonNull(onError));
    }
}
