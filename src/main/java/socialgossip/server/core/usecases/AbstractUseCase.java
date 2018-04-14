package socialgossip.server.core.usecases;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Abstract class that allows safe implementations of an {@link UseCase}.
 * @param <I> is the {@link UseCase} input type.
 * @param <O> is the {@link UseCase} produced result type.
 * @param <E> is the {@link ErrorsHandler} for errors produced
 *            by the {@link UseCase} execution.
 */
public abstract class AbstractUseCase<I, O, E extends ErrorsHandler>
        implements UseCase<I, O, E> {

    /**
     * Executes the {@link UseCase} logic, and guarantees that input,
     * success callback and errors handler are valid and not-null.
     * @param input is the not-null {@link UseCase} input.
     * @param onSuccess is the not-null {@link UseCase} success callback.
     * @param errors is the not-null {@link UseCase} errors callback.
     */
    protected abstract void onExecute(I input, Consumer<O> onSuccess, E errors);

    public final void execute(final I input, final Consumer<O> onSuccess, final E errors) {
        this.onExecute(Objects.requireNonNull(input),
                       Objects.requireNonNull(onSuccess),
                       Objects.requireNonNull(errors));
    }
}
