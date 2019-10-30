import Action.Sender;
import Action.Listener;
import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        if (2 != args.length) {
            System.out.println("You should enter IP and port");
        }

        Sender sender = new Sender(args[0], Integer.parseInt(args[1]));
        Thread myThread = new Thread(sender);
        myThread.start();

        try {
            Listener listener = new Listener(args[0], Integer.parseInt(args[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
