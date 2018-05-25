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
import socialgossip.server.usecases.login.LoginErrors;
import socialgossip.server.usecases.login.LoginOutput;
import socialgossip.server.usecases.login.LoginUseCase;

import java.io.Writer;
import java.util.Objects;

public class LoginController
        extends AbstractController<LoginJSONInput, LoginOutput> {

    private final LoginUseCase<LoginOutput, LoginErrors> interactor;

    public LoginController(final Presenter<LoginOutput> presenter,
                           final LoginUseCase<LoginOutput, LoginErrors> interactor) {
        super(presenter);
        this.interactor = Objects.requireNonNull(interactor);
    }

    @Override
    protected LoginJSONInput produceInputObject(final String requestId, final JSONObject object) {
        return new LoginJSONInput(requestId, object);
    }

    @Override
    protected void executor(final TCPRequest request, final LoginJSONInput input, final Writer responseWriter) {
        interactor.execute(
                input.withIpAddress(request.getIpAddress()),
                (IOConsumer<LoginOutput>) (o) -> {
                    responseWriter.write(presenter.getOkResponse(o).toJSONString());
                },
                new LoginErrors() {
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
                }
        );
    }
}
