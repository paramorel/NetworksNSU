import Server.*;

import java.io.IOException;

public class Main {
    public static void main (String[] args ) throws IOException {
        if (1 != args.length) {
            System.err.println("Invalid argument");
            return;
        }

        Server server = new Server(Integer.parseInt(args[0]));
        server.receiveFile();
    }
}