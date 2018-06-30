package network.kotlin.flow9.net.networkbasic.socket;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient {

    public static void start() {
        try {
            int port = 3001;
            Socket socket = new Socket("localhost", port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("hello world");
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String obj = (String) ois.readObject();
            Log.d("MainActivity", "서버에서 받은 메시지 : "+obj);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
