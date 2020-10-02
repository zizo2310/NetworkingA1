package main.HTTP;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetRequest extends HRequest{
    static String METHOD_NAME = "GET";

    protected String getTheHost() {
        return String.format("Host: %s", this.url.getHost());
    }
    public List<String> getOutput() {
        List<String> lines = new ArrayList<String>();
        lines.add(this.getStartOfLine());
        lines.add(this.getTheHost());
        lines.addAll(this.getHeaders());
        lines.add("");
        return lines
                .stream()
                .map(line -> line + "\r\n")
                .collect(Collectors.toList());
    }

    public GetRequest(URL url) {
        super(METHOD_NAME, url);
    }



}

