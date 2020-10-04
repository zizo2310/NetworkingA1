package main.HTTP;

import java.net.URL;

public class GetRequest extends HRequest{
    static String METHOD_NAME = "GET";

    public GetRequest(URL url) {

        super(METHOD_NAME, url);
    }

    public boolean Parse (String[] args){
        boolean errorOccurred = false;
        for(int i = 1; i < args.length-1; i++){
            switch(args[i]){
                case "-v":
                    this.verboseEnabled = true;
                    break;
                case "-h":
                    String [] header = args[i + 1].split(":");
                    if (header.length == 2 && (i+1)!=args.length-1) {
                        this.header.put(header[0], header[1]);
                        i++;
                    }
                    else{
                        errorOccurred = true;
                    }
                    break;
                default:
                    errorOccurred = true;
                    break;
            }
        }
        return errorOccurred;
    }
}

