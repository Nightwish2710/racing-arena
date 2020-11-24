package clientnetwork;

import clientdatamodel.ClientDataModel;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientNetwork {
    private Socket clientSocket;
    private DataOutputStream outStream;
    private DataInputStream inStream;
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
        //while (true) {
        //    receiverThread = new ReceiverThread(inStream);
        //    receiverThread.start();
        //}
    }

    public void send(int cmd, ClientDataModel clientDataModel) {
        try {
            System.out.println(this.getClass().getSimpleName() + ": sending username, password");
            outStream.write(clientDataModel.pack(cmd));

            String responseLine = inStream.readLine();
            System.out.println(this.getClass().getSimpleName() + " server says: " + responseLine);

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
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
