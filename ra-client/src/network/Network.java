package network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Logger;

public class Network {
    private Socket clientSocket;
    private BufferedWriter outStream;
    private BufferedReader inStream;

    public Network() {
        clientSocket = null;
        outStream = null;
        inStream = null;
    }

    public void connect() {
        try {
            // Gửi yêu cầu kết nối tới Server đang lắng nghe
            // trên máy 'localhost' cổng 7777.
            clientSocket = new Socket(NetworkConfig.SERVER_HOST, NetworkConfig.SERVER_PORT);

            // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
            outStream = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
            inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Unknown host named " + NetworkConfig.SERVER_HOST);
            return;
        } catch (IOException e) {
            System.err.println("I/O Error in connection to " + NetworkConfig.SERVER_HOST);
            return;
        } finally {
            // notify of successful connection
            System.out.println(this.getClass().getSimpleName() + ": notify of successful connection");
        }
    }

    public void send(String msg) {
        try {
            // Ghi dữ liệu vào luồng đầu ra của Socket tại Client.
            outStream.write("OKAY");
            outStream.newLine(); // kết thúc dòng
            outStream.flush();  // đẩy dữ liệu đi.

            // Đọc dữ liệu trả lời từ phía server
            // Bằng cách đọc luồng đầu vào của Socket tại Client.
            String responseLine;
            while ((responseLine = inStream.readLine()) != null) {
                System.out.println("Server: " + responseLine);
                if (responseLine.indexOf("OK") != -1) {
                    break;
                }
            }

            outStream.close();
            inStream.close();
            clientSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
