package Server;

import java.io.*;
import java.net.Socket;

public class User implements Runnable {
    Socket socket;

    public User(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(inputStream));

            String fileName = fromClient.readLine();
            long fileSize = Long.parseLong(fromClient.readLine());

            PrintWriter toClient = new PrintWriter(socket.getOutputStream());


            System.out.println("I get " + fileName + " and he have size " + fileSize + " bytes");
            File dir = new File("./upload/");

            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    System.err.println("Creating directory error");

                    toClient.println(Boolean.FALSE.toString());
                    toClient.flush();
                    socket.close();
                    fromClient.close();
                    toClient.close();
                    inputStream.close();
                    return;
                }
            }

            File file = new File(dir.getAbsolutePath() + "/" + fileName);

            toClient.println(Boolean.TRUE.toString());
            toClient.flush();
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];

            long onePeriod = 0;
            long startTime = System.currentTimeMillis();
            long time = startTime;

            for (long i = 0; i < fileSize; ) {
                int read = inputStream.read(buffer);

                i += read;
                onePeriod += read;

                fileOutputStream.write(buffer, 0, read);

                if (System.currentTimeMillis() - time > 2000) {

                    time = System.currentTimeMillis();

                    System.out.println("File" + fileName +
                            " Instant speed, MB/s: " + String.format("%.2f", (double) onePeriod / 1024 / 1024 / 3) +
                            " Average speed, MB/s: " + String.format("%.2f", (double) i / 1024 / 1024 / ((double) (time - startTime) / 1000)));

                    onePeriod = 0;
                }
            }

            toClient.println(Boolean.TRUE.toString());
            toClient.flush();
            System.out.println("Server: upload of \"" + fileName + "\" is done");

            socket.close();
            fromClient.close();
            toClient.close();
            inputStream.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}