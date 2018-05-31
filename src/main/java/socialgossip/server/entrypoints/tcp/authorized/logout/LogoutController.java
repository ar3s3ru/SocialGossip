package socialgossip.server.entrypoints.tcp.authorized.logout;

import org.json.simple.JSONObject;
import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.session.SessionNotFoundException;
import socialgossip.server.entrypoints.tcp.AbstractController;
import socialgossip.server.entrypoints.tcp.IOConsumer;
import socialgossip.server.entrypoints.tcp.TCPRequest;
import socialgossip.server.presentation.Presenter;
import socialgossip.server.usecases.UseCase;
import socialgossip.server.usecases.logout.LogoutErrors;
import socialgossip.server.usecases.logout.LogoutUseCase;

import java.io.Writer;
import java.util.function.Consumer;

public class LogoutController
        extends AbstractController<Void, LogoutUseCase.Input, Boolean, LogoutErrors> {

    public LogoutController(final Presenter<Void> presenter,
                            final UseCase<LogoutUseCase.Input, Boolean, LogoutErrors> interactor) {
        super(presenter, interactor);
    }

    @Override
    protected LogoutUseCase.Input produceInputObject(final TCPRequest request, final JSONObject object) {
        return new LogoutJSONInput(request.getId(), object);
    }

    @Override
    protected Consumer<Boolean> produceOutputConsumer(TCPRequest request, Writer responseWriter) {
        return ((IOConsumer<Boolean>) (b) -> {
            responseWriter.write(presenter.getOkResponse(null).toJSONString());
        });
    }

    @Override
    protected LogoutErrors produceErrorHandler(TCPRequest request, Writer responseWriter) {
        return new LogoutErrors() {
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
        };
    }
}
