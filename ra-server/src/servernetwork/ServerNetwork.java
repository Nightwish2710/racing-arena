package servernetwork;

import serverGUI.ServerGUIConfig;
import serverdatamodel.ServerDataModel;
import serverobject.ServerGameConfig;
import serverobject.ServerGameMaster;

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
    private ServerNetworkThread serverNetworkThread;
    private ExecutorService networkPool;

    // Singleton
    private static ServerNetwork serverNetwork = null;
    public static ServerNetwork getInstance() {
        if (serverNetwork == null) {
            serverNetwork = new ServerNetwork();
        }
        return serverNetwork;
    }

    public ServerNetwork() {
        this.serverNetworkThread = null;
        this.networkPool = Executors.newFixedThreadPool(1);
        serverNetwork = this;
    }

    public boolean isNetworkOpenning() {
        return serverNetworkThread!=null && serverNetworkThread.isOpenning();
    }

    public void closeNetwork() {
        serverNetworkThread.close();
        networkPool.shutdown();
    }

    public void openServerSocket() {
        serverNetworkThread = new ServerNetworkThread();
        networkPool.execute(serverNetworkThread);
    }

    public static class ServerNetworkThread implements Runnable {
        private ServerSocket serverSocket;
        private ExecutorService clientPool;
        private int cSocketID;
        private HashMap<Integer, ServerCSocketThread> cSocketThreads;

        public ServerNetworkThread() {
            this.serverSocket = null;
            this.cSocketThreads = new HashMap<>();
            this.cSocketID = 0;
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

                    this.cSocketID += 1;
                    ServerCSocketThread clientThread = new ServerCSocketThread(cSocket, this.cSocketID, this);

                    this.subscribeClientSocket(this.cSocketID, clientThread);
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

        public void subscribeClientSocket (int cSocketThreadID, ServerCSocketThread cSockThread) {
            this.cSocketThreads.put(cSocketThreadID, cSockThread);
        }

        public void unSubscribeClientSocket (int cSocketThreadID) {
            this.cSocketThreads.remove(cSocketThreadID);
        }

        public boolean isOpenning() {
            return !this.serverSocket.isClosed();
        }

        public void close() {
            try {
                this.serverSocket.close();
                this.clientPool.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void signalAllClients(ServerDataModel data, int callerID, boolean ignoreCaller) {
            if (ignoreCaller) {
                for (Map.Entry<Integer, ServerCSocketThread> entry : this.cSocketThreads.entrySet()) {
                    if (entry.getKey() != callerID && entry.getValue().getsRacerName() != null) {
                        entry.getValue().reply(data);
                    }
                }
            }
            else {
                for (Map.Entry<Integer, ServerCSocketThread> entry : this.cSocketThreads.entrySet()) {
                    if (entry.getValue().getsRacerName() != null) {
                        entry.getValue().reply(data);
                    }
                }
            }
        }

        public int getNumberOfClient() {
            return cSocketThreads.size();
        }
    }
}