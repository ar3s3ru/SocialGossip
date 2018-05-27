package socialgossip.server.entrypoints.tcp.authorized.logout;

import org.json.simple.JSONObject;
import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.session.SessionNotFoundException;
import socialgossip.server.entrypoints.tcp.AbstractController;
import socialgossip.server.entrypoints.tcp.IOConsumer;
import socialgossip.server.entrypoints.tcp.TCPRequest;
import socialgossip.server.presentation.Presenter;
import socialgossip.server.usecases.logout.LogoutErrors;
import socialgossip.server.usecases.logout.LogoutUseCase;

import java.io.Writer;
import java.util.Objects;

public class LogoutController extends AbstractController<LogoutJSONInput, Void> {
    private final LogoutUseCase<Boolean, LogoutErrors> interactor;

    public LogoutController(final Presenter<Void> presenter,
                            final LogoutUseCase<Boolean, LogoutErrors> interactor) {
        super(presenter);
        this.interactor = Objects.requireNonNull(interactor);
    }

    @Override
    protected LogoutJSONInput produceInputObject(final String requestId, final JSONObject object) {
        return new LogoutJSONInput(requestId, object);
    }

    @Override
    protected void executor(final TCPRequest request,
                            final LogoutJSONInput input,
                            final Writer responseWriter) {
        interactor.execute(
                input,
                ((IOConsumer<Boolean>) (b) -> {
                    responseWriter.write(presenter.getOkResponse(null).toJSONString());
                }),
                new LogoutErrors() {
                    @Override
                    public void onSessionNotFound(SessionNotFoundException e) {
                        ((IOConsumer<Throwable>) (t) -> {
                            responseWriter.write(presenter.getFailResponse(t).toJSONString());
                        }).accept(e);
                    }

                    @Override
                    public void onUnauthorizedAccess(UnauthorizedException e) {
                        ((IOConsumer<Throwable>) (t) -> {
                            responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                        }).accept(e);
                    }

                    @Override
                    public void onGatewayError(GatewayException e) {
                        ((IOConsumer<Throwable>) (t) -> {
                            responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                        }).accept(e);
                    }

                    @Override
                    public void onError(Exception e) {
                        // TODO: finish this
                    }
                });
    }
}
