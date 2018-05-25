package socialgossip.server.entrypoints.tcp;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TCPRequest {
    private final String      id;
    private final String      body;
    private final InetAddress ipAddress;

    private Map<String, Object> context;

    public TCPRequest(final String requestId, final String requestBody, final InetAddress ipAddress) {
        this.id        = Objects.requireNonNull(requestId);
        this.body      = requestBody;
        this.ipAddress = Objects.requireNonNull(ipAddress);
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void contextAdd(final String key, final Object object) {
        if (context == null) {
            context = new HashMap<>();
        }
        context.put(Objects.requireNonNull(key), Objects.requireNonNull(object));
    }

    public Object contextGet(final String key) {
        return Optional.ofNullable(key).map(
                k -> Optional.ofNullable(context).map(c -> c.get(k)).orElse(null)
        ).orElse(null);
    }
}
