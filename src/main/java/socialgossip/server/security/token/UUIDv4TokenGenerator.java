package socialgossip.server.security.token;

import java.util.UUID;

public final class UUIDv4TokenGenerator implements TokenGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
