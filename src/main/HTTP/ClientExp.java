package main.HTTP;
import java.io.IOException;
import java.net.UnknownHostException;

public interface ClientExp {
    void connect(String host, int port) throws UnknownHostException, IOException;
    String read() throws IOException;
    void send(HRequest httpRequest) throws IOException;
    void stopConnection() throws IOException;
    String getUri();
    String sendAndRead(HRequest httpRequest) throws IOException;
}
