package main.HTTP;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Response extends HMessage {

    private int status;
    private String reasonPhrase;
    private String body;


    public Response(int status, String reasonPhrase, String body) {
        super();
        this.status = status;
        this.reasonPhrase = reasonPhrase;
        this.body = body;
    }

    public Response() {

    }

    public static Response fromRaw(String raw) {
        Response response = new Response();
        addStatusAndReasonPhraseToResponseFromRaw(raw, response);
        addHeadersToResponseFromRaw(raw, response);
        addBodyToResponseFromRaw(raw, response);
        return response;
    }

    public int getStatus() {
        return this.status;
    }

    private static void addStatusAndReasonPhraseToResponseFromRaw(String raw, Response response) {
        Pattern startLineRegex = Pattern.compile("^(HTTP\\/\\d.\\d)\\s(\\d+)\\s((\\w+\\s?)*)$");
        String startLine = raw.split("\n")[0];
        Matcher matcher = startLineRegex.matcher(startLine);
        if (matcher.find()) {
            response.status = Integer.parseInt(matcher.group(2));
            response.reasonPhrase = matcher.group(3);
        }
    }

    private static void addHeadersToResponseFromRaw(String raw, Response response) {
        Map<String, String> headers = Arrays.stream(raw.split("\n"))
                .filter(line -> line.matches("([\\w-]+):(.*)"))
                .collect(Collectors.toMap(
                        header -> header.split(":")[0],
                        header -> header.split(":")[1])
                );
        response.header = headers;
    }

    private static void addBodyToResponseFromRaw(String raw, Response response) {
        String[] split = raw.split("\n\n");
        String body = "";
        if (split.length > 1) {
            body = split[1];
        }
        response.body = body;
    }

    @Override
    protected String getStartOfLine() {
        return String.format("%s %d %s", this.HTTP_VERSION, this.status, this.reasonPhrase);
    }

    @Override
    public String toString() {
        String headers = getHeaders().stream().collect(Collectors.joining("\n"));
        return String.format("%s\n%s\n\n%s", getStartOfLine(), headers, this.body);
    }

}
