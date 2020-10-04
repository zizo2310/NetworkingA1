package main.HTTP;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

public class httpc {
    public static void main(String[] args) {
        if(args.length > 0){
            String command = args[0].toLowerCase();
            try {
                switch (command) {
                    case "get":
                        URL url = new URL(args[args.length - 1]);
                        GetRequest getRequest = new GetRequest(url);
                        Parse(getRequest, args);
                        CreateConnection(getRequest);
                        /*URL url = new URL(args[args.length - 1]);
                        if(url.getQuery()==null) {
                            GetRequest getRequest = new GetRequest(url);
                            Parse(getRequest, args);
                            CreateConnection(getRequest);
                        }
                        else{
                            PostRequest postRequest = new PostRequest(url);
                            Parse(postRequest, args);
                            postRequest.withData(url.getQuery());
                            CreateConnection(postRequest);
                        }*/
                        break;
                    case "post":
                        URL url2 = new URL(args[args.length - 1]);
                        PostRequest postRequest = new PostRequest(url2);
                        Parse(postRequest, args);
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

    public static void Parse(HRequest request, String[] args){
        for(int i = 1; i < args.length-1; i++){
            switch(args[i]){
                case "-v":
                    request.verboseEnabled = true;
                    break;
                case "-h":
                    String [] header = args[i + 1].split(":");
                    if (header.length == 2 && (i+1)!=args.length-1) {
                        request.header.put(header[0], header[1]);
                        i++;
                    }
                    else{
                        Help();
                        System.exit(0);
                    }
                    break;
                case "-d":
                    if(request.emptyBody) {
                        if (request instanceof PostRequest) {
                            if ((i+1)!=args.length-1) {
                                request.withData(args[i + 1]);
                            }
                            request.emptyBody = false;
                            i++;
                        }
                        else {
                            Help();
                            System.exit(0);
                        }
                    }
                    //Multiple instances of -d or -f options
                    else{
                        Help();
                        System.exit(0);
                    }
                    break;
                case "-f":
                    if(request.emptyBody) {
                        if(request instanceof PostRequest) {
                            if((i+1)!=args.length){
                                File file= new File("src\\main\\HTTP\\"+args[i+1]);
                                request.withFile(file);
                            }
                            request.emptyBody = false;
                            i++;
                        }
                        else{
                            Help();
                            System.exit(0);
                        }
                    }
                    //Multiple instances of -d or -f options
                    else{
                        Help();
                        System.exit(0);
                    }
                    break;
            }
        }
    }

    public static void CreateConnection(HRequest request){
        try{
            InetAddress web = InetAddress.getByName(request.url.getHost());
            Socket socket = new Socket(web, 80);
            OutputStreamWriter outputWriter = new OutputStreamWriter(socket.getOutputStream());
            InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
            String strMessage = "";

            String requestMessage = request.method + " " + request.url + " " + request.HTTP_VERSION;
            for(int i =0; i < request.getHeaders().size(); i++){
                requestMessage+="\r\n" + request.getHeaders().get(i);
            }
            requestMessage+= "\r\n\r\n";
            if(request.data!=null){
                requestMessage+= request.data;
            }
            outputWriter.write(requestMessage);
            outputWriter.flush();
            int message = inputReader.read();
            while(inputReader.ready()){
                strMessage+=(char) message;
                message = inputReader.read();
            }
            if(!request.verboseEnabled){
                String [] strArray = strMessage.split("\r\n\r\n");
                for(int i =1; i < strArray.length; i++) {
                    System.out.println(strArray[i]);
                }
            }
            else {
                System.out.println(strMessage);
            }
            inputReader.close();
            outputWriter.close();
            socket.close();
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

