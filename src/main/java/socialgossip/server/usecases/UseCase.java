package socialgossip.server.usecases;

import java.util.function.Consumer;

/**
 * Interface that represents an application use-case.
 * @param <I> is the {@link UseCase} input type.
 * @param <O> is the {@link UseCase} output type, e.g. the result produced by the use-case.
 */
@FunctionalInterface
public interface UseCase<I extends UseCase.Input, O> {
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
     * @param onError is the callback that handles eventual error cases.
     */
    void execute(I input, Consumer<O> onSuccess, Consumer<Throwable> onError);
}