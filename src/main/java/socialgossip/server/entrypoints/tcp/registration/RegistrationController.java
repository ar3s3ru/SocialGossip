package socialgossip.server.entrypoints.tcp.registration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.entrypoints.tcp.AbstractController;
import socialgossip.server.entrypoints.tcp.IOConsumer;
import socialgossip.server.entrypoints.tcp.TCPServer;
import socialgossip.server.presentation.Presenter;
import socialgossip.server.usecases.registration.RegistrationErrors;
import socialgossip.server.usecases.registration.RegistrationUseCase;

import java.io.IOException;
import java.io.Writer;
import java.util.IllformedLocaleException;
import java.util.Objects;

public class RegistrationController
        extends AbstractController<RegistrationJSONInput> {
    public static final String REGISTRATION_OPCODE = "register";

    private final RegistrationUseCase<Void, RegistrationErrors> interactor;
    private final Presenter<String> presenter;

    public RegistrationController(final TCPServer tcpServer,
                                  final RegistrationUseCase<Void, RegistrationErrors> interactor,
                                  final Presenter<String> presenter) {
        super(tcpServer);
        this.interactor = Objects.requireNonNull(interactor);
        this.presenter  = Objects.requireNonNull(presenter);
    }

    @Override
    public String OPCODE() {
        return REGISTRATION_OPCODE;
    }

    @Override
    public RegistrationJSONInput parseInput(final String requestId, final String json)
            throws IOException, ParseException {
        final JSONParser parser = new JSONParser();
        // TODO: add validation
        return new RegistrationJSONInput(requestId, (JSONObject) parser.parse(json));
    }

    @Override
    public void handle(final String requestId, final String requestBody, final Writer responseWriter) {
        try {
            final RegistrationJSONInput input = parseInput(requestId, requestBody);
            interactor.execute(
                    input,
                    (IOConsumer<Void>) (v) -> {
                        responseWriter.write(presenter.getOkResponse(input.getUsername()).toJSONString());
                    },
                    new RegistrationErrors() {
                        @Override
                        public void onInvalidLanguage(final IllformedLocaleException e) {
                            ((IOConsumer<Throwable>) (t) -> {
                                responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                            }).accept(e);
                        }

                        @Override
                        public void onInvalidUser(final InvalidUserException e) {
                            ((IOConsumer<Throwable>) (t) -> {
                                responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                            }).accept(e);
                        }

                        @Override
                        public void onUserAlreadyExists(final UserAlreadyExistsException e) {
                            ((IOConsumer<Throwable>) (t) -> {
                                responseWriter.write(presenter.getFailResponse(t).toJSONString());
                            }).accept(e);
                        }

                        @Override
                        public void onGatewayError(final GatewayException e) {
                            ((IOConsumer<Throwable>) (t) -> {
                                responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                            }).accept(e);
                        }

                        @Override
                        public void onInvalidPassword(final InvalidPasswordException e) {
                            ((IOConsumer<Throwable>) (t) -> {
                                responseWriter.write(presenter.getFailResponse(t).toJSONString());
                            }).accept(e);
                        }

                        @Override
                        public void onError(Exception e) {
                            // TODO: finish this
                        }
                    });
        } catch (ParseException e) {
            ((IOConsumer<Throwable>) (t) -> {
                responseWriter.write(presenter.getErrorResponse(t).toJSONString());
            }).accept(e);
        } catch (IOException e) {

        }
    }
}
