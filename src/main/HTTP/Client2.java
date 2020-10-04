package main.HTTP;
import java.io.IOException;

public class Client2 implements ClientExp2{
    private final int DEFAULT_PORT = 80;


    public Response send(HRequest request) throws IOException {
        Client client = new Client(request.url.getHost(), DEFAULT_PORT);
        String raw = client.sendAndRead(request);
        Response response = Response.fromRaw(raw);
        return response;
    }

}
