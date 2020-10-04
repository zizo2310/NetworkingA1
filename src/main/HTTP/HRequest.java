package main.HTTP;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public abstract class HRequest {

    protected String method;
    protected URL url;
    protected String data;
    //make the version as a constant
    public final String HTTP_VERSION = "HTTP/1.0";
    //creating a hashmap tothe header to reuse after
    Map<String, String> header;
    protected boolean verboseEnabled;
    protected boolean emptyBody;

    public HRequest(String method, URL url) {
        this.header = new HashMap<>();
        this.method = method;
        this.url = url;
        this.verboseEnabled = false;
        this.emptyBody = true;
    }

    public HRequest withData(String data) {
        this.data = data;
        this.addContentLengthToHeaders();
        this.emptyBody = false;
        return this;
    }

    public HRequest withFile(File file) {
        this.data = GetDataFromFile(file);
        this.addContentLengthToHeaders();
        this.emptyBody = false;
        return this;
    }

    private String GetDataFromFile(File file){
        String data = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputReader = new InputStreamReader(fileInputStream);
            while (inputReader.ready()) {
                int message = inputReader.read();
                data += (char) message;
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return data;
    }

    private void addContentLengthToHeaders() {
        //Not sure if it should be long will decide later
        long contentLength = 0;

        if (this.data != null) {
            contentLength = this.data.getBytes().length;
        }
        this.header.put("Content-Length", Long.toString(contentLength));
    }

    public List<String> getHeaders() {
        List<String> formattedHeaders = new ArrayList<String>();
        this.header.forEach((name, value) -> formattedHeaders.add(String.format("%s: %s", name, value)));
        return formattedHeaders;
    }

    public Map<String, String> getHeadersMap() {
        return this.header;
    }

}

