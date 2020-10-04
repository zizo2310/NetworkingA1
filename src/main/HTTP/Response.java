package main.HTTP;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Response {

    protected String message;

    public Response(HRequest request) {
        this.message = ReadRequest(request);
    }

    public String getMessage() {
        return this.message;
    }

    private String ReadRequest(HRequest request){
        String responseMessage = "";
        try{
            InetAddress web = InetAddress.getByName(request.url.getHost());
            Socket socket = new Socket(web, 80);
            OutputStreamWriter outputWriter = new OutputStreamWriter(socket.getOutputStream());
            InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());

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
                responseMessage+=(char) message;
                message = inputReader.read();
            }
            if(!request.verboseEnabled){
                String [] strArray = responseMessage.split("\r\n\r\n");
                String parsedMessage = "";
                for(int i =1; i < strArray.length; i++) {
                    parsedMessage += strArray[i];
                }
                responseMessage = parsedMessage;
            }
            inputReader.close();
            outputWriter.close();
            socket.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return responseMessage;
    }
}
