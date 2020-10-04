package main.HTTP;

import java.net.MalformedURLException;
import java.net.URL;

public class httpc {
    public static void main(String[] args) {
        if(args.length > 0){
            String command = args[0].toLowerCase();
            boolean errorOccurred;
            try {
                switch (command) {
                    case "get":
                        URL url = new URL(args[args.length - 1]);
                        if(url.getQuery()==null) {
                            GetRequest getRequest = new GetRequest(url);
                            errorOccurred = getRequest.Parse(args);
                            if(errorOccurred){
                                Help();
                                System.exit(0);
                            }
                            Response response = new Response(getRequest);
                            System.out.println(response.getMessage());
                        }
                        else{
                            PostRequest postRequest = new PostRequest(url);
                            errorOccurred = postRequest.Parse(args);
                            if(errorOccurred){
                                Help();
                                System.exit(0);
                            }
                            postRequest.withData(url.getQuery());
                            Response response = new Response(postRequest);
                            System.out.println(response.getMessage());
                        }
                        break;
                    case "post":
                        URL url2 = new URL(args[args.length - 1]);
                        PostRequest postRequest = new PostRequest(url2);
                        errorOccurred = postRequest.Parse(args);
                        if(errorOccurred){
                            Help();
                            System.exit(0);
                        }
                        Response response2 = new Response(postRequest);
                        System.out.println(response2.getMessage());
                        break;
                    case "help":
                        //Prints the help information for the command
                        if (args.length > 1) {
                            Help(args[1]);
                        }
                        //Prints the general help information
                        else {
                            Help();
                        }
                        break;
                    default:
                        System.out.println("Invalid command. Use 'httpc help' for more information");
                }
            }
            catch(MalformedURLException e) {
                System.out.println("Invalid URL");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("Try 'httpc help' for more information");
        }
    }

    public static void Help(){
        System.out.println("\nhttpc is a curl-like application but supports HTTP protocol only.\n");
        System.out.println("Usage:");
        System.out.println("\thttpc command [arguments]");
        System.out.println("the commands are:");
        System.out.println("\tget \texecutes a HTTP GET request and prints the response.");
        System.out.println("\tpost \texecutes a HTTP POST request and prints the response.");
        System.out.println("\thelp \tprints this screen.\n");
        System.out.println("Use \"httpc help [command]\" for more information about a command.");
    }

    public static void Help(String command){
        if(command.equals("get")){
            System.out.println("\nusage: httpc get [-v] [-h key:value] URL\n");
            System.out.println("Get executes a HTTP GET request for a given URL.\n");
            System.out.println("\t-v \t\tPrints the detail of the response such as protocol, status, and headers.");
            System.out.println("\t-h key:value \tAssociates headers to HTTP Request with the format 'key:value'.");
        }
        else if(command.equals("post")){
            System.out.println("\nusage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\n");
            System.out.println("Post executes a HTTP POST request for a given URL with inline data or from file.\n");
            System.out.println("\t-v \t\tPrints the detail of the response such as protocol, status, and headers.");
            System.out.println("\t-h key:value \tAssociates headers to HTTP Request with the format 'key:value'.");
            System.out.println("\t-d string \tAssociates an inline data to the body HTTP POST request.");
            System.out.println("\t-f file \tAssociates the content of a file to the body HTTP POST request.\n");
            System.out.println("Either [-d] or [-f] cam be used but not both.");
        }
        else{
            System.out.println("Invalid command. Try 'httpc help' for more information");
        }
    }
}

