package clientnetwork;

import clientGUI.ClientGUI;
import clientdatamodel.ClientDataModel;
import clientdatamodel.receive.CReceiveLogin;
import clientobject.ClientGameMaster;
import clientobject.ClientOpponent;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

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
        this.clientSocket = null;
        this.outStream = null;
        this.inStream = null;
        this.receiverThread = null;
        this.clientNetwork = this;
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
        } catch (IOException e) { // if no connection
            ClientGUI.getInstance().setVisible(false);
            ClientGUI.getInstance().noOpenConnection();

            System.err.println("I/O Exception in connection to " + ClientNetworkConfig.SERVER_HOST);
            return;
        }

        // notify of successful connection
        System.out.println(this.getClass().getSimpleName() + ": notification of successful connection");
        // open client UI
        ClientGUI.getInstance().setVisible(true);

        // Start receiver thread, in case we might need
        receiverThread = new ClientReceiverThread(inStream);
        executor = new Thread(receiverThread);
        executor.start();
    }

    public boolean isConnected() {
        return clientSocket.isConnected();
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
                if (clientSocket.isClosed() || ClientNetwork.getInstance().getClientSocket().isClosed()) {
                    return;
                }

                try {
                    int cmd = inStream.readInt();

                    int lData = inStream.available();
                    byte[] bytes = new byte[lData];
                    inStream.read(bytes);

                    // Switch on command id
                    switch (cmd) {
                        case ClientNetworkConfig.CMD.CMD_LOGIN:
                            receiveLogin(bytes);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        private void receiveLogin(byte[] bytes) {
            CReceiveLogin cReceiveLogin = new CReceiveLogin();
            cReceiveLogin.unpack(bytes);

            switch (cReceiveLogin.getEventFlag()) {
                case ClientNetworkConfig.LOGIN_FLAG.NO_MORE_SLOTS:
                    // update UI
                    System.out.println(getClass().getSimpleName() + ": NO_MORE_SLOTS");
                    break;
                case ClientNetworkConfig.LOGIN_FLAG.USERNAME_TAKEN:
                    // update UI
                    System.out.println(getClass().getSimpleName() + ": USERNAME_TAKEN");
                    break;
                case ClientNetworkConfig.LOGIN_FLAG.DUPLICATED_LOGIN:
                    // update UI
                    System.out.println(getClass().getSimpleName() + ": DUPLICATED_LOGIN");
                    break;
                case ClientNetworkConfig.LOGIN_FLAG.ERROR:
                    // update UI
                    System.out.println(getClass().getSimpleName() + ": ERROR");
                    break;
                case ClientNetworkConfig.LOGIN_FLAG.SUCCESS:
                    // confirm this racer, record his opponent array
                    ClientGameMaster.getInstance().getcRacer().setNumOfVictory(cReceiveLogin.getRacerVictory());
                    ClientGameMaster.getInstance().setNumOfRacers(cReceiveLogin.getNumOfRacers());
                    ClientGameMaster.getInstance().setcOpponents(cReceiveLogin.getcOpponents());

                    // update UI
                    ClientGUI.getInstance().disableComponentAfterJoinServer();
                    System.out.println(getClass().getSimpleName() + ": SUCCESS");
                    for (Map.Entry<String, ClientOpponent> opps : cReceiveLogin.getcOpponents().entrySet()) {
                        System.out.println(getClass().getSimpleName() + " got: " + opps.getKey() + " - " + opps.getValue().getStatusFlag());
                    }
                    break;
                default:
                    break;
            }
        }


    }
}


