package main.HTTP;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public abstract class HMessage {

    //make the version as a constant
    public final String HTTP_VERSION = "HTTP/1.0";

    Map<String, String> header;

    //creating a hashmap tothe header to reuse after

    public HMessage() {
        this.header = new HashMap<>();
    }

    abstract String getStartOfLine();

    public List<String> getHeaders() {
        List<String> formattedHeaders = new ArrayList<String>();
        this.header.forEach((name, value) -> formattedHeaders.add(String.format("%s: %s", name, value)));
        return formattedHeaders;
    }

    public Map<String, String> getHeadersMap() {
        return this.header;
    }
}
