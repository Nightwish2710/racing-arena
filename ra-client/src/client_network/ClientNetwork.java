package client_network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientNetwork {
    private Socket clientSocket;
    private BufferedWriter outStream;
    private BufferedReader inStream;
    private ClientReceiverThread receiverThread;

    public ClientNetwork() {
        clientSocket = null;
        outStream = null;
        inStream = null;
        receiverThread = null;
    }

    public void connect() {
        try {
            // Gửi yêu cầu kết nối tới Server đang lắng nghe
            // trên máy 'localhost' cổng 7777.
            clientSocket = new Socket(ClientNetworkConfig.SERVER_HOST, ClientNetworkConfig.SERVER_PORT);

            // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
            outStream = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
            inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Unknown host named " + ClientNetworkConfig.SERVER_HOST);
            return;
        } catch (IOException e) {
            System.err.println("I/O Error in connection to " + ClientNetworkConfig.SERVER_HOST);
            return;
        }

        // notify of successful connection
        System.out.println(this.getClass().getSimpleName() + ": notify of successful connection");
        // Update GUI


        // Start receiver thread, in case we might need
        //while (true) {
        //    receiverThread = new ReceiverThread(inStream);
        //    receiverThread.start();
        //}
    }

    public void send(int cmd, String msg) {
        try {
            outStream.write(msg);
            outStream.newLine();
            outStream.flush();

            String responseLine = inStream.readLine();
            System.out.println(this.getClass().getSimpleName() + "server says: " + responseLine);

            if (msg == "q") {
                outStream.close();
                clientSocket.close();
            }
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
