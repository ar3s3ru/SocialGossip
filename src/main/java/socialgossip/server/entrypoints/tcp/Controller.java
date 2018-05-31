package socialgossip.server.entrypoints.tcp;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Writer;

public interface Controller<InputType> {
    void handle(TCPRequest request, Writer responseWriter);
    InputType parseInput(TCPRequest request) throws IOException, ParseException;
}
