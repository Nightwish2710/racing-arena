package clientnetwork;

import clientGUI.ClientGUI;

import clientdatamodel.ClientDataModel;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientNetwork {
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
        this.clientSocket = null;
        this.outStream = null;
        this.inStream = null;
        this.receiverThread = null;
        this.clientNetwork = this;
    }

    public Socket getClientSocket() { return this.clientSocket; }

    public void connect() {
        try {
            // send connection request to Server
            this.clientSocket = new Socket(ClientNetworkConfig.SERVER_HOST, ClientNetworkConfig.SERVER_PORT);

            // output stream at Client (send data to server)
            this.outStream = new DataOutputStream(clientSocket.getOutputStream());

            // input stream at Client (receive data from server)
            this.inStream = new DataInputStream(clientSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Unknown host named " + ClientNetworkConfig.SERVER_HOST);
            return;
        } catch (IOException e) { // if no connection
            System.err.println("I/O Exception in connection to " + ClientNetworkConfig.SERVER_HOST);

            ClientGUI.getInstance().setVisible(false);
            ClientGUI.getInstance().turnOnNoOpenConnectionPane();

            return;
        }

        // notify of successful connection
        System.out.println(this.getClass().getSimpleName() + ": notification of successful connection");

        // open client UI
        ClientGUI.getInstance().setVisible(true);
        // turn off error pane
        ClientGUI.getInstance().turnOffNoOpenConnectionPane();

        // Start receiver thread, in case we might need
        receiverThread = new ClientReceiverThread(this.clientSocket, this.inStream);
        executor = new Thread(receiverThread);
        executor.start();
    }

    public boolean isConnected() {
        return this.clientSocket.isConnected();
    }

    public void send(ClientDataModel clientDataModel) {
        try {
            System.out.println(this.getClass().getSimpleName() + ": sending username, password");
            outStream.write(clientDataModel.pack());
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("I/O Exception: " + e);
        }
    }

    public void disconnect() {
        try {
            this.outStream.writeInt(ClientNetworkConfig.CMD.DISCONNECT);

            this.receiverThread.stopReceiverThread();
            this.executor.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}