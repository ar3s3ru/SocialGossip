package socialgossip.server.entrypoints.tcp.login;

import org.json.simple.JSONObject;
import socialgossip.server.entrypoints.tcp.JSONInput;
import socialgossip.server.usecases.login.LoginUseCase;
import socialgossip.server.validation.ValidationException;

import java.net.InetAddress;
import java.util.Optional;

public class LoginJSONInput extends JSONInput implements LoginUseCase.Input {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private final String username;
    private final String password;
    private InetAddress ipAddress;

    public LoginJSONInput(final String requestId, final JSONObject jsonObject) {
        super(requestId, jsonObject);
        this.username = (String) jsonObject.get(USERNAME_FIELD);
        this.password = (String) jsonObject.get(PASSWORD_FIELD);
    }

    @Override
    public void validate() throws ValidationException {
        Optional.ofNullable(username).orElseThrow(() -> new ValidationException(USERNAME_FIELD, "can't be null"));
        Optional.ofNullable(password).orElseThrow(() -> new ValidationException(PASSWORD_FIELD, "can't be null"));
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public LoginJSONInput withIpAddress(final InetAddress ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    @Override
    public InetAddress getIpAddress() {
        return ipAddress;
    }
}
