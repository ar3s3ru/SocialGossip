package socialgossip.server.entrypoints.tcp.login;

import org.json.simple.JSONObject;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.entrypoints.tcp.JSONInput;
import socialgossip.server.usecases.login.LoginUseCase;

import java.net.InetAddress;
import java.util.function.Function;

public class LoginJSONInput extends JSONInput implements LoginUseCase.Input {
    private final String username;
    private final String password;

    private Function<Session, NotificationHandler> friendshipHandler;
    private InetAddress ipAddress;

    public LoginJSONInput(final String requestId, final JSONObject jsonObject) {
        super(requestId, jsonObject);
        this.username = (String) jsonObject.get("username");
        this.password = (String) jsonObject.get("password");
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

    public LoginJSONInput withFriendshipsHandler(final Function<Session, NotificationHandler> handler) {
        this.friendshipHandler = handler;
        return this;
    }

    @Override
    public Function<Session, NotificationHandler> getFriendshipsHandler() {
        return friendshipHandler;
    }
}
