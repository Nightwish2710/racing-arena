package servernetwork;

import serverdatamodel.SRAccount;
import serverobject.ServerGameMaster;

import java.io.*;
import java.net.Socket;

public class ServerCSocketThread implements Runnable{
    private int clientNumber;
    private Socket socketOfServer;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private ServerNetwork.ServerNetworkThread parentThread;

    public ServerCSocketThread(Socket _socketOfServer, int _clientNumber, ServerNetwork.ServerNetworkThread _parentThread) {
        this.clientNumber = _clientNumber;
        this.socketOfServer = _socketOfServer;
        this.parentThread = _parentThread;
        System.out.println(this.getClass().getSimpleName() + " new connection with client# " + this.clientNumber + " at " + socketOfServer);
    }

    @Override
    public void run() {
        try {
            // Server socket I/O
            inStream = new DataInputStream(socketOfServer.getInputStream());
            outStream = new DataOutputStream(socketOfServer.getOutputStream());

            while (true) {
                int cmd = inStream.readInt();
                if (cmd == ServerNetworkConfig.CMD.DISCONNECT) {
                    break;
                }

                int lData = inStream.available();
                byte[] bytes = new byte[lData];
                inStream.read(bytes);

                // Switch on command id
                switch (cmd) {
                    case ServerNetworkConfig.CMD.CMD_LOGIN:
                        handleLogin(bytes, this.outStream, this.parentThread);
                        break;
                }

                // <SWICTH CASE> create handler for each of these cases
                if (cmd == ServerNetworkConfig.CMD.CMD_LOGIN) {

                    // check if sdAccount is valid

                    // notify back
                    // outStream.writeChars("Individually, created account with Username: " + sdAccount.getUsername() + " - Password: " + sdAccount.getPassword() + '\n');

                    // bulk notify back
                    //this.parentThread.signalAllClients("New user added: " + srAccount.getUsername());
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void handleLogin(byte[] bytes, DataOutputStream outStream, ServerNetwork.ServerNetworkThread parentThread) {
        SRAccount srAccount = new SRAccount();
        srAccount.unpack(bytes);
        System.out.println(this.getClass().getSimpleName()+": request login: " + srAccount.getUsername() + ", " + srAccount.getPassword());

        // check if there is available slots
        if (this.parentThread.getNumberOfClient() < ServerGameMaster.getInstance().getNumberOfRacer()) {
            // if yes
            String queryUser = "SELECT * FROM ";
            //      check if username exists in database
            //      if it is, check if password match
            //          if password match, existing user, send individually (success login) and bulk (update number of racers to all)
            //          if password not match, username duplicate error, send individually (username has been taken)
            //      if it is not, new user, send individually (success login) and bulk (update number of racers to all)
        }
        // if no, not record login, send individually (no more slots)
    }

    public void reply (String msg) {
        try {
            outStream.writeChars(msg);
            System.out.println(this.getClass().getSimpleName()+": send down "+ msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
