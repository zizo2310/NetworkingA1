package main.HTTP;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetRequest extends HRequest{
    static String METHOD_NAME = "GET";

    public GetRequest(URL url) {

        super(METHOD_NAME, url);
    }

}

