package servernetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetwork {
    // Singleton
    private static ServerNetwork serverNetwork = null;
    public static ServerNetwork getInstance() {
        if (serverNetwork == null) {
            serverNetwork = new ServerNetwork();
        }
        return serverNetwork;
    }

    public ServerNetwork() {
    }

    public void openServerSocket() {
        new Thread(new ServerNetworkThread()).start();
    }

    private class ServerNetworkThread implements Runnable {
        private ServerSocket serverSocket;
        private int clientNumber;

        public ServerNetworkThread() {
            this.serverSocket = null;
            this.clientNumber = 0;
        }

        @Override
        public void run() {
            System.out.println("Server is waiting to accept user...");

            try {
                this.serverSocket = new ServerSocket(ServerNetworkConfig.SERVER_PORT);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(1);
            }

            try {
                while (true) {
                    Socket cSocket = null;
                    try {
                        cSocket = this.serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new Thread(new ServerCSocketThread(cSocket, this.clientNumber++)).start();
                }
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}