package clientnetwork;

import clientdatamodel.ClientDataModel;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientNetwork {
    public Socket getClientSocket() {
        return clientSocket;
    }

    private Socket clientSocket;
    private DataOutputStream outStream;
    private DataInputStream inStream;
    private Thread executor;
    private ClientReceiverThread receiverThread;

    // Singleton
    private static ClientNetwork clientNetwork = null;
    public static ClientNetwork getInstance() {
        if (clientNetwork == null) {
            clientNetwork = new ClientNetwork();
            clientNetwork.connect();
        }
        return clientNetwork;
    }

    public ClientNetwork() {
        clientSocket = null;
        outStream = null;
        inStream = null;
        receiverThread = null;
        clientNetwork = this;
    }

    public void connect() {
        try {
            // send connection request to Server
            clientSocket = new Socket(ClientNetworkConfig.SERVER_HOST, ClientNetworkConfig.SERVER_PORT);

            // output stream at Client (send data to server)
            outStream = new DataOutputStream(clientSocket.getOutputStream());

            // input stream at Client (receive data from server)
            inStream = new DataInputStream(clientSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Unknown host named " + ClientNetworkConfig.SERVER_HOST);
            return;
        } catch (IOException e) {
            System.err.println("I/O Exception in connection to " + ClientNetworkConfig.SERVER_HOST);
            return;
        }

        // notify of successful connection
        System.out.println(this.getClass().getSimpleName() + ": notification of successful connection");

        // Start receiver thread, in case we might need
        receiverThread = new ClientReceiverThread(inStream);
        executor = new Thread(receiverThread);
        executor.start();
    }

    public boolean isConnected() {
        return clientSocket.isConnected();
    }

    public void send(int cmd, ClientDataModel clientDataModel) {
        try {
            System.out.println(this.getClass().getSimpleName() + ": sending username, password");
            outStream.write(clientDataModel.pack(cmd));
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("I/O Exception: " + e);
        }
    }

    public void disconnect() {
        try {
            outStream.writeInt(ClientNetworkConfig.CMD.DISCONNECT);
            outStream.close();
            inStream.close();
            executor.interrupt();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ClientReceiverThread implements Runnable {
        private DataInputStream inStream;

        public ClientReceiverThread(DataInputStream _inStream) {
            this.inStream = _inStream;
        }

        @Override
        public void run() {
            while (true) {
                if (ClientNetwork.getInstance().getClientSocket().isClosed()) {
                    System.out.println(this.getClass().getSimpleName() + " CLOSED ");
                    return;
                }
                String responseLine = null;
                try {
                    responseLine = inStream.readLine();
                    if (responseLine.contains("quit")) {
                        break;
                    }
                    System.out.println(this.getClass().getSimpleName() + " server says: " + responseLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


