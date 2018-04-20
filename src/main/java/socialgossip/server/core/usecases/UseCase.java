package socialgossip.server.core.usecases;

import java.util.function.Consumer;

/**
 * Interface that represents an application use-case.
 * @param <I> is the {@link UseCase} input type.
 * @param <O> is the {@link UseCase} output type, e.g. the result produced by the use-case.
 * @param <E> is the {@link ErrorsHandler} object, that allows to handle error cases
 *            that happened during the {@link UseCase} execution.
 */
@FunctionalInterface
public interface UseCase<I extends UseCase.Input, O, E extends ErrorsHandler> {
    /**
     * Represents the input of an {@link UseCase}.
     * Every requests to an {@link UseCase} must have a request id, so that we can
     * trace back every actions of the request.
     */
    interface Input {
        String getRequestId();
    }

    /**
     * Executes the {@link UseCase} logic.
     * @param input is the {@link UseCase} input.
     * @param onSuccess is the callback executed when the execution terminated successfully.
     * @param errors is the callback that handles eventual error cases.
     */
    void execute(I input, Consumer<O> onSuccess, E errors);
}