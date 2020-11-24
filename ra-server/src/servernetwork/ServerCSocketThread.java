package servernetwork;

import org.h2.util.IOUtils;
import serverdatamodel.SDAccount;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class ServerCSocketThread extends Thread{
    private int clientNumber;
    private Socket socketOfServer;
    private DataInputStream inStream;
    private DataOutputStream outStream;

    public ServerCSocketThread(Socket socketOfServer, int clientNumber) {
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;

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

                // <SWICTH CASE> create handler for each of these cases
                if (cmd == ServerNetworkConfig.CMD.CMD_LOGIN) {
                    SDAccount sdAccount = new SDAccount();
                    sdAccount.unpack(bytes);
                    System.out.println(this.getClass().getSimpleName()+": user login with username, password: "+ sdAccount.getUsername()+ ", " + sdAccount.getPassword());

                    // check if sdAccount is valid
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // notify back
                    outStream.writeChars("Created account with Username: " + sdAccount.getUsername() + ", Password: " + sdAccount.getPassword() + '\n');
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
