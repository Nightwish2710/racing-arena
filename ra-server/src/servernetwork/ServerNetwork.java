package servernetwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetwork {
    private ServerSocket serverSocket;
    private int clientNumber;

    private static ServerNetwork serverNetwork = null;
    public static ServerNetwork getInstance() {
        if (serverNetwork == null) {
            serverNetwork = new ServerNetwork();
        }
        return serverNetwork;
    }

    public ServerNetwork() {
        this.serverSocket = null;
        this.clientNumber = 0;
    }

    public void openServerSocket() {
        System.out.println("Server is waiting to accept user...");

        try {
            serverSocket = new ServerSocket(ServerNetworkConfig.SERVER_PORT);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            while (true) {
                Socket cSocket = null;
                try {
                    cSocket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new ServerCSocketThread(cSocket, clientNumber++).start();
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