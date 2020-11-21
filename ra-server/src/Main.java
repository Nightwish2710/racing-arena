import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String args[]) throws IOException {

        ServerSocket listener = null;

        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;

        // open a Server at port 3628
        try {
            listener = new ServerSocket(3628);
        }
        catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            while (true) {
                // accept request from Client and receiver object Socket at Server
                Socket socketOfServer = listener.accept();
                new Main.ServiceThread(socketOfServer, clientNumber++).start();
            }
        }
        finally {
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

            log("New connection with client# " + this.clientNumber + " at " + socketOfServer);
        }

        @Override
        public void run() {

            try { // open output stream on Socket at Server
                BufferedReader is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

                while (true) { // receiver data sent to Server from Client
                    String line = is.readLine();

                    if (line != null) {
                        // write to output stream of Socker at Server
                        os.write(">> " + line);
                        os.newLine();
                        os.flush(); // push data to Client

                        // end of conversation
                        if (line.equals("QUIT")) {
                            os.write(">> OK");
                            os.newLine();
                            os.flush();
                            break;
                        }
                    }
                }
            }
            catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
}
