package network.kotlin.flow9.net.networkbasic.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void start() {
        try {
            int port = 3001;
            System.out.println("Starting Java Socket Server");
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Listening at port "+port+"...");

            while (true) {
                Socket socket = ss.accept();
                InetAddress clientHost = socket.getLocalAddress();
                int clientPort = socket.getPort();
                System.out.println("Client Connected. host : "+clientHost+", port : "+clientPort);

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object obj = ois.readObject();
                System.out.print("Input : "+obj);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(obj+" from server");
                oos.flush();
                socket.close();
                ss.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
