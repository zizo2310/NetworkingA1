package main.HTTP;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;

public class PostRequest extends HRequest {

    private static String HTTP_METHOD = "POST";


    public PostRequest(URL url) {
        super(HTTP_METHOD, url);
    }

    public boolean Parse(String[] args){
        boolean errorOccurred = false;
        File outputFile = null;
        for(int i = 1; i < args.length-1; i++){
            switch(args[i]){
                case "-v":
                    this.verboseEnabled = true;
                    break;
                case "-h":
                    String [] header = args[i + 1].split(":");
                    //Checks if the next string is a key-value pair and isn't the URL
                    if (header.length == 2 && (i+1)!=args.length-1) {
                        this.header.put(header[0], header[1]);
                        i++;
                    }
                    else{
                        errorOccurred = true;
                    }
                    break;
                case "-d":
                    if(this.emptyBody) {
                        if ((i+1)!=args.length-1) {
                            this.withData(args[i + 1]);
                            i++;
                        }
                        else{
                            errorOccurred = true;
                        }
                    }
                    //Multiple instances of -d or -f options
                    else{
                        errorOccurred = true;
                    }
                    break;
                case "-f":
                    if(this.emptyBody) {
                        if((i+1)!=args.length-1){
                            File file= new File("src\\main\\HTTP\\"+args[i+1]);
                            this.withFile(file);
                            i++;
                        }
                        else{
                            errorOccurred = true;
                        }
                    }
                    //Multiple instances of -d or -f options
                    else{
                        errorOccurred = true;
                    }
                    break;
                case "-o":
                    if((i+1)!=args.length-1){
                        outputFile = new File("src\\main\\HTTP\\"+args[i+1]);
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
        if(outputFile!=null && this.data!=null){
            try{
            WriteInOutputFile(outputFile, data);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

        return errorOccurred;
    }

    public void WriteInOutputFile(File file, String data) throws Exception{
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(data);
        fileWriter.close();
    }
}
