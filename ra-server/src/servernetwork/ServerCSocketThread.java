package servernetwork;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ServerCSocketThread extends Thread{
    private int clientNumber;
    private Socket socketOfServer;
    private BufferedReader inStream;
    private BufferedWriter outStream;

    public ServerCSocketThread(Socket socketOfServer, int clientNumber) {
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;

        System.out.println(this.getClass().getSimpleName() + " new connection with client# " + this.clientNumber + " at " + socketOfServer);
    }

    @Override
    public void run() {
        try {
            // Server socket I/O
            inStream = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            outStream = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

            while (true) {
                // Đọc dữ liệu tới server (Do client gửi tới).
                String line = inStream.readLine();
                if (line != null) {
                    System.out.println("FROM CLIENT " + line);
                    // Ghi vào luồng đầu ra của Socket tại Server.
                    // (Nghĩa là gửi tới Client).
                    outStream.write("Replying to >> " + line);
                    // Kết thúc dòng
                    outStream.newLine();

                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Đẩy dữ liệu đi
                    outStream.flush();

                    // Nếu người dùng gửi tới QUIT (Muốn kết thúc trò chuyện).
                    if (line.equals("q")) {
                        outStream.write(">> OK");
                        outStream.newLine();
                        outStream.flush();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
