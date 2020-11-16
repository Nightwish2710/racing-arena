import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String args[]) throws IOException {

        ServerSocket listener = null;

        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;


        // Mở một ServerSocket tại cổng 7777.
        // Chú ý bạn không thể chọn cổng nhỏ hơn 1023 nếu không là người dùng
        // đặc quyền (privileged users (root)).
        try {
            listener = new ServerSocket(7777);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            while (true) {


                // Chấp nhận một yêu cầu kết nối từ phía Client.
                // Đồng thời nhận được một đối tượng Socket tại server.

                Socket socketOfServer = listener.accept();
                new Main.ServiceThread(socketOfServer, clientNumber++).start();
            }
        } finally {
            listener.close();
        }

    }

    private static void log(String message) {
        System.out.println(message);
    }

    private static class ServiceThread extends Thread {

        private int clientNumber;
        private Socket socketOfServer;

        public ServiceThread(Socket socketOfServer, int clientNumber) {
            this.clientNumber = clientNumber;
            this.socketOfServer = socketOfServer;

            // Log
            log("New connection with client# " + this.clientNumber + " at " + socketOfServer);
        }

        @Override
        public void run() {

            try {


                // Mở luồng vào ra trên Socket tại Server.
                BufferedReader is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

                while (true) {
                    // Đọc dữ liệu tới server (Do client gửi tới).
                    String line = is.readLine();

                    // Ghi vào luồng đầu ra của Socket tại Server.
                    // (Nghĩa là gửi tới Client).
                    os.write(">> " + line);
                    // Kết thúc dòng
                    os.newLine();
                    // Đẩy dữ liệu đi
                    os.flush();

                    // Nếu người dùng gửi tới QUIT (Muốn kết thúc trò chuyện).
                    if (line.equals("QUIT")) {
                        os.write(">> OK");
                        os.newLine();
                        os.flush();
                        break;
                    }
                }

            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
}
