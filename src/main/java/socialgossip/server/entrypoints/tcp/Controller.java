package socialgossip.server.entrypoints.tcp;

import org.json.simple.parser.ParseException;
import socialgossip.server.usecases.UseCase;

import java.io.IOException;
import java.io.Writer;

public interface Controller<InputType extends UseCase.Input> {
    void handle(TCPRequest request, Writer responseWriter);
    InputType parseInput(TCPRequest request) throws IOException, ParseException;
}
