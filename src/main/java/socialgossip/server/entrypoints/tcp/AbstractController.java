package socialgossip.server.entrypoints.tcp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.presentation.Presenter;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import java.util.logging.Logger;

public abstract class AbstractController<InputType, OutputType>
        implements Controller<InputType> {
    protected final Logger LOG = Logger.getLogger(this.getClass().getName());
    protected final Presenter<OutputType> presenter;

    public AbstractController(final Presenter<OutputType> presenter) {
        this.presenter = Objects.requireNonNull(presenter);
    }

    protected abstract InputType produceInputObject(final String requestId, final JSONObject object);

    @Override
    public InputType parseInput(final String requestId, final String json)
            throws IOException, ParseException {
        final JSONParser parser = new JSONParser();
        return produceInputObject(requestId, (JSONObject) parser.parse(json));
    }

    protected abstract void executor(final TCPRequest request, final InputType input, final Writer responseWriter);

    @Override
    public void handle(final TCPRequest request, final Writer responseWriter) {
        try {
            final InputType input = parseInput(request.getId(), request.getBody());
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
