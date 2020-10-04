package main.HTTP;
import java.io.*;
import java.net.*;
import java.util.List;

public class Client {

    private Socket socket;
    private BufferedWriter output;
    private BufferedReader input;
    private String host;
    private int port;

    public Client() {

    }

    public Client(String host, int port) throws IOException {
        this.connect(host, port);
        this.host = host;
        this.port = port;
    }


    public void connect(String host, int port) throws IOException {
        this.socket = new Socket(InetAddress.getByName(host), port);
        this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF8"));
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }


    public String read() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = this.input.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }


    public void send(HRequest httpRequest) throws IOException {
        writeOutputLinesToSocket(httpRequest.getOutput(), this.output);
        File file;
        if ((file = httpRequest.getFile()) != null) {
            writeFileToSocket(file, this.output);
        }
        this.output.flush();
    }

    private void writeOutputLinesToSocket(List<String> outputLines, BufferedWriter output) throws IOException {
        for (String line : outputLines) {
            System.out.print(line);
            output.write(line);
        }
    }

    private void writeFileToSocket(File file, BufferedWriter output) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        int content;
        while ((content = fileInputStream.read()) > 0) {
            output.write(content);
        }
    }


    public void stopConnection() throws IOException {
        this.output.close();
        this.input.close();
        this.socket.close();
    }

    public String getUri() {
        return String.format("%s:%s", this.host, this.port);
    }

    public String sendAndRead(HRequest httpRequest) throws IOException {
        this.send(httpRequest);
        String response = this.read();
        this.stopConnection();

        return response;
    }
}
