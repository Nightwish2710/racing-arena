package network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Network {
    private Socket clientSocket;
    private BufferedWriter outStream;
    private BufferedReader inStream;
    private ReceiverThread receiverThread;

    public Network() {
        clientSocket = null;
        outStream = null;
        inStream = null;
        receiverThread = null;
    }

    public void connect() {
        try {
            // send connection request to Server on "localhost" with port 3628
            clientSocket = new Socket(NetworkConfig.SERVER_HOST, NetworkConfig.SERVER_PORT);

            // output stream at Client (send data to server)
            outStream = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // input stream at Client (receive data from server)
            inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host with host: " + NetworkConfig.SERVER_HOST);
            return;
        }
        catch (IOException e) {
            System.err.println("I/O Exception with host: " + NetworkConfig.SERVER_HOST);
            return;
        }
        finally {
            // notify of successful connection
            System.out.println(this.getClass().getSimpleName() + ": notification of successful connection.");
        }

        // notify of successful connection
        System.out.println(this.getClass().getSimpleName() + ": notify of successful connection");
        // Update GUI


        // Start receiver thread, in case we might need
        //while (true) {
        //    receiverThread = new ReceiverThread(inStream);
        //    receiverThread.start();
        //}
    }

    public void send(int cmd, String msg) {
        try {
            outStream.write(msg);
            outStream.newLine();
            outStream.flush();

            String responseLine = inStream.readLine();
            System.out.println(this.getClass().getSimpleName() + "server says: " + responseLine);

            if (msg == "q") {
                outStream.close();
                clientSocket.close();
            }
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
