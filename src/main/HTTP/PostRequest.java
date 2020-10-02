package main.HTTP;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostRequest extends HRequest {

    private static String HTTP_METHOD = "POST";


    public PostRequest(URL url) {
        super(HTTP_METHOD, url);
    }

    protected String getHostLine() {
        return String.format("Host: %s", this.url.getHost());
    }

    public List<String> getOutput() {
        List<String> lines = new ArrayList<String>();
        lines.add(this.getStartOfLine());
        lines.add(this.getHostLine());
        lines.addAll(this.getHeaders());
        lines.add("");
        if (this.data != null) {
            System.out.println(this.data);
            lines.add(this.data);
        }

        return lines
                .stream()
                .map(line -> line + "\r\n")
                .collect(Collectors.toList());
    }
}
