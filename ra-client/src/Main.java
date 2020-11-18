import network.Network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Main {
    private static Network network;

    public static void main(String[] args) {
        // connect to server
        connectToServer();

        network.send("OKAYYY");
    }

    private static void connectToServer() {
        network = new Network();
        network.connect();
    }
}
