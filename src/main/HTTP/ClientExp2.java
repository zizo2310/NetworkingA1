package main.HTTP;

import java.io.IOException;

public interface ClientExp2 {
    Response send(HRequest request) throws IOException;
}
