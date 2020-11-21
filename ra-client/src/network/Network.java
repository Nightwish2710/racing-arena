package network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Logger;

public class Network {
    private Socket clientSocket;
    private BufferedWriter outStream;
    private BufferedReader inStream;

    public Network() {
        clientSocket = null;
        outStream = null;
        inStream = null;
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
    }

    public void send(String msg) {
        try {
            // write data into output stream of Socket at Client
            outStream.write("OKAY");
            outStream.newLine();
            outStream.flush();  // push data to Server

            // read data sent from Server by reading input stream of Socket at Client
            String responseLine;
            while ((responseLine = inStream.readLine()) != null) {
                System.out.println("Server: " + responseLine);
                if (responseLine.indexOf("OK") != -1) {
                    break;
                }
            }

            outStream.close();
            inStream.close();
            clientSocket.close();
        }
        catch (UnknownHostException e) {
            System.err.println("Try to connect to Unknown Host: " + e);
        }
        catch (IOException e) {
            System.err.println("I/O Exception: " + e);
        }
    }
}
