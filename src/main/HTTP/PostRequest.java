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

}
