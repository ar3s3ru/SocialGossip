package socialgossip.server.entrypoints.tcp.login;

import org.json.simple.JSONObject;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.entrypoints.tcp.AbstractController;
import socialgossip.server.entrypoints.tcp.IOConsumer;
import socialgossip.server.entrypoints.tcp.TCPRequest;
import socialgossip.server.presentation.Presenter;
import socialgossip.server.usecases.UseCase;
import socialgossip.server.usecases.login.LoginErrors;
import socialgossip.server.usecases.login.LoginOutput;
import socialgossip.server.usecases.login.LoginUseCase;

import java.io.Writer;
import java.util.function.Consumer;

public class LoginController
        extends AbstractController<LoginOutput, LoginUseCase.Input, LoginOutput, LoginErrors> {

    public LoginController(final Presenter<LoginOutput> presenter,
                           final UseCase<LoginUseCase.Input, LoginOutput, LoginErrors> interactor) {
        super(presenter, interactor);
    }

    @Override
    protected LoginUseCase.Input produceInputObject(final TCPRequest request, final JSONObject object) {
        return new LoginJSONInput(request.getId(), object);
    }

    @Override
    protected Consumer<LoginOutput> produceOutputConsumer(final TCPRequest request, final Writer responseWriter) {
        return (IOConsumer<LoginOutput>) (o) -> {
            responseWriter.write(presenter.getOkResponse(o).toJSONString());
        };
    }

    @Override
    protected LoginErrors produceErrorHandler(TCPRequest request, Writer responseWriter) {
        return new LoginErrors() {
            @Override
            public void onUserNotFound(UserNotFoundException e) {
                ((IOConsumer<Throwable>) (t) -> {
                    responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                }).accept(e);
            }

            @Override
            public void onSessionAlreadyExists(SessionAlreadyExistsException e) {
                ((IOConsumer<Throwable>) (t) -> {
                    responseWriter.write(presenter.getFailResponse(t).toJSONString());
                }).accept(e);
            }

            @Override
            public void onGatewayError(GatewayException e) {
                ((IOConsumer<Throwable>) (t) -> {
                    responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                }).accept(e);
            }

            @Override
            public void onInvalidPassword(InvalidPasswordException e) {
                ((IOConsumer<Throwable>) (t) -> {
                    responseWriter.write(presenter.getFailResponse(t).toJSONString());
                }).accept(e);
            }

            @Override
            public void onError(Exception e) {
                // TODO: finish this
            }
        };
    }
}
