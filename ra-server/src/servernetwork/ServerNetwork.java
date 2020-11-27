package servernetwork;

import serverGUI.ServerGUIConfig;
import serverobject.ServerGameConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static class ServerNetworkThread implements Runnable {
        private ServerSocket serverSocket;
        private int clientNumber;
        private ExecutorService clientPool;
        private HashMap<Integer, ServerCSocketThread> cSocketThreads;

        public ServerNetworkThread() {
            this.serverSocket = null;
            this.clientNumber = 0;
            this.cSocketThreads = new HashMap<>();
            this.clientPool = Executors.newFixedThreadPool(ServerGameConfig.MAX_NUM_OF_RACERS);
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
                    ServerCSocketThread clientThread = new ServerCSocketThread(cSocket, this.clientNumber++, this);
                    this.cSocketThreads.put(this.clientNumber, clientThread);
                    this.clientPool.execute(clientThread);
                }
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.clientPool.shutdown();
            }
        }

        public void signalAllClients(String data) {
            for (Map.Entry<Integer, ServerCSocketThread> entry : this.cSocketThreads.entrySet()) {
                entry.getValue().reply("Hit all client with case "+ data + '\n');
            }
        }

        public int getNumberOfClient() {
            return cSocketThreads.size();
        }
    }
}