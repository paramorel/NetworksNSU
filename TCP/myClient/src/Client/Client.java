package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class Client {

    private String host;
    private int serverPort;

    public Client(String host, int port) {
        serverPort = port;
        host = host;
    }

    public void sendFile(String filename) {
        try {
            File file = new File(filename);
            InetAddress serverAddress = InetAddress.getByName(host);
            Socket clientSocket = new Socket(serverAddress, serverPort);
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter toServer = new PrintWriter(outputStream);

            toServer.println(file.getName());
            toServer.println(file.length());
            toServer.flush();

            BufferedReader readerFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            boolean returned = Boolean.parseBoolean(readerFromServer.readLine());

            if (returned) {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];

                int read = 0;

                while (0 < inputStream.available() && -1 != read) {
                    read = inputStream.read(buffer);
                    outputStream.write(buffer, 0, read);
                }

                outputStream.flush();

                returned = Boolean.parseBoolean(readerFromServer.readLine());
                if (returned) {
                    System.out.print("Upload finish successful\n");
                } else {
                    System.out.println("Upload finish failure\n");
                }
            } else {
                System.out.println("Error with connect to server\n");
            }
            clientSocket.close();
            toServer.close();
            outputStream.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}