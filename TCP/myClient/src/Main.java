import Client.Client;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("Invalid argument. Enter IP, port, filename");
            return;
        }

        File file = new File(args[2]);

        Client client = new Client(args[0], Integer.parseInt(args[1]));
        client.sendFile(args[2]);
    }
}