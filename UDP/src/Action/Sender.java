package Action;

import java.io.IOException;
import java.net.*;

public class Sender implements Runnable{
    private String host;
    private int portGroup;
    private InetAddress ipGroup;


    public Sender(String host, int portGroup) {
        this.host = host;
        this.portGroup = portGroup;
    }

    private void sendMessage(DatagramSocket datagramSocket) throws IOException {
        InetAddress ipGroup = InetAddress.getByName(host);
        byte[] message = InetAddress.getLocalHost().getHostAddress().getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(message, message.length, ipGroup, portGroup);
        datagramSocket.send(datagramPacket);
    }

    @Override
    public void run(){
        try (DatagramSocket datagramSocket = new DatagramSocket(0)){
            ipGroup = InetAddress.getByName(host);
            while(true){
                sendMessage(datagramSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
