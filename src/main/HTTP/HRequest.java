package main.HTTP;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
public abstract class HRequest extends HMessage {

    private String method;
    protected URL url;
    protected String data;
    protected File file;

    public HRequest(String method, URL url) {
        super();
        this.method = method;
        this.url = url;
    }

    public HRequest withHeaders(Map<String, String> headers) {
        this.header = headers;
        return this;
    }

    public HRequest withData(String data) {
        this.data = data;
        this.addContentLengthToHeaders();
        return this;
    }

    public File getFile() {
        return this.file;
    }

    public HRequest withFile(File file) {
        this.file = file;
        this.addContentLengthToHeaders();
        return this;
    }

    private void addContentLengthToHeaders() {

        //Not sure if it should be long will decide later
        long contentLength = 0;

        if (this.data != null) {
            contentLength = this.data.getBytes().length;
        } else if (this.file != null) {
            contentLength = this.file.length();
        }
        this.header.put("Content-Length", Long.toString(contentLength));
    }

    public abstract List<String> getOutput();
    protected abstract String getTheHost();

    protected String getStartOfLine() {
        return String.format("%s %s %s", this.method.toUpperCase(), getRequestPathWithQueryParams(), this.HTTP_VERSION);
    }

    private String getRequestPathWithQueryParams() {
        return (this.url.getPath().isEmpty() ? this.url.getPath() : "/") + (this.url.getQuery() != null ? "?" + this.url.getQuery() : "");
    }


}

