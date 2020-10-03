package main.HTTP;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class httpc {
    public static void main(String[] args) {
        if(args.length > 0){
            String command = args[0].toLowerCase();
            try {
                URL url = new URL(args[args.length - 1]);
                switch (command) {
                    case "get":
                        GetRequest getRequest = new GetRequest(url);
                        Parse(getRequest, args);
                        System.out.println(getRequest.getOutput());
                        CreateConnection(getRequest);
                        break;
                    case "post":
                        PostRequest postRequest = new PostRequest(url);
                        Parse(postRequest, args);
                        System.out.println(postRequest.getOutput());
                        CreateConnection(postRequest);
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
                        break;
                }
            }
            catch(Exception e) {
                System.out.println("Invalid URL");
            }
        }
        else {
            System.out.println("Try 'httpc help' for more information");
        }
    }

    public static void Parse(HRequest request, String[] parameters){
        Map<String, String> headers = new HashMap<>();
        for(int i = 1; i < parameters.length-1; i++){
            switch(parameters[i]){
                case "-v":
                    System.out.println("-v Should print the details of the response such as protocol and status");
                    break;
                case "-h":
                    String [] header = parameters[i + 1].split(":");
                    headers.put(header[0], header[1]);
                    i++;
                    break;
                case "-d":
                    if(request instanceof PostRequest) {
                        request.withData(parameters[i + 1]);
                        i++;
                    }
                    else{
                        System.out.println("Invalid command. Try 'httpc help' for more information");
                    }
                    break;
                case "-f":
                    if(request instanceof PostRequest) {
                        File file= new File(parameters[i+1]);
                        request.withFile(file);
                    }
                    else{
                        System.out.println("Request is not a ");
                    }
                    break;
            }
        }
        request.withHeaders(headers);
    }

    public static void CreateConnection(HRequest request){
        try{
            InetAddress web = InetAddress.getByName(request.url.getHost());
            Socket socket = new Socket(web, 80);
            OutputStreamWriter outputWriter = new OutputStreamWriter(socket.getOutputStream());
            InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
            List<String> output = request.getOutput();
            for(int i = 0; i < output.size(); i++){
                outputWriter.write(output.indexOf(i));
            }
            outputWriter.flush();
            int message = inputReader.read();
            while(inputReader.ready()){
                System.out.print((char) message);
                message = inputReader.read();
            }
            inputReader.close();
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    public static void Help(){
        //Print help message directly or read from a file
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

