package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    //private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public void receiveFile() throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Thread thread = new Thread(new User(socket));
                    thread.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
