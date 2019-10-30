package Action;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Listener {
    private String host;
    private int portGroup;
    private InetAddress ipGroup;
    public static MulticastSocket multicastSocket;
    private HashMap<SocketAddress, Long> senders= new HashMap<>();


    public Listener(String host, int portGroup) throws IOException {
        this.host = host;
        this.portGroup = portGroup;
        try (MulticastSocket multicastSocket = new MulticastSocket(portGroup)) {
            ipGroup = InetAddress.getByName(host);
            multicastSocket.joinGroup(ipGroup);
            receiveMessage(multicastSocket);
        }
    }

    private void check(){
        ArrayList<SocketAddress> dead = new ArrayList<>();
        for (HashMap.Entry<SocketAddress, Long> entry : senders.entrySet()){
            if (System.currentTimeMillis() - entry.getValue() > 2000){
                dead.add(entry.getKey());
            }
        }

        for (SocketAddress inactive : dead){
            senders.remove(inactive);
        }

        if(!dead.isEmpty()){
            printMyCopy();
        }
        dead.clear();
    }

    private void printMyCopy(){
        System.out.println("Count of me: " + senders.size());
        for (HashMap.Entry<SocketAddress, Long> entry : senders.entrySet()){
            System.out.println(entry.getKey());
        }
        System.out.println("\n");
    }

    private void receiveMessage(MulticastSocket multicastSocket) throws IOException {
        byte[] buf = new byte[100];
        while (true){
            check();
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
            multicastSocket.receive(datagramPacket);
            SocketAddress ipOfSender = datagramPacket.getSocketAddress();
            if(senders.put(ipOfSender, System.currentTimeMillis()) == null){
                printMyCopy();
            }
        }
    }
}
