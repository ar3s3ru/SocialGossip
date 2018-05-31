package socialgossip.server.entrypoints.tcp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.presentation.Presenter;
import socialgossip.server.usecases.ErrorsHandler;
import socialgossip.server.usecases.UseCase;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class AbstractController<
        // Presenter could optionally use a different argument type from the usecase output.
        PresenterType,
        // Use case type classes.
        InputType extends UseCase.Input, OutputType, ErrorType extends ErrorsHandler
        > implements Controller<InputType> {

    protected final Logger LOG = Logger.getLogger(this.getClass().getName());

    protected final Presenter<PresenterType> presenter;
    protected final UseCase<InputType, OutputType, ErrorType> interactor;

    public AbstractController(final Presenter<PresenterType> presenter,
                              final UseCase<InputType, OutputType, ErrorType> interactor) {
        this.presenter  = Objects.requireNonNull(presenter);
        this.interactor = Objects.requireNonNull(interactor);
    }

    protected abstract InputType produceInputObject(final TCPRequest request, final JSONObject object);
    protected abstract Consumer<OutputType> produceOutputConsumer(final TCPRequest request, final Writer responseWriter);
    protected abstract ErrorType produceErrorHandler(final TCPRequest request, final Writer responseWriter);

    protected void executor(final TCPRequest request, final InputType input, final Writer responseWriter) {
        interactor.execute(
                input,
                produceOutputConsumer(request, responseWriter),
                produceErrorHandler(request, responseWriter)
        );
    }

    @Override
    public InputType parseInput(final TCPRequest request) throws IOException, ParseException {
        final JSONParser parser = new JSONParser();
        return produceInputObject(request, (JSONObject) parser.parse(request.getBody()));
    }

    @Override
    public void handle(final TCPRequest request, final Writer responseWriter) {
        try {
            final InputType input = parseInput(request);
            executor(request, input, responseWriter);
        } catch (ParseException e) {
            AppLogger.exception(LOG, request::getId, e);
            ((IOConsumer<Throwable>) (t) -> {
                responseWriter.write(presenter.getErrorResponse(t).toJSONString());
            }).accept(e);
        } catch (IOException e) {
            AppLogger.exception(LOG, request::getId, e);
        }
    }
}
