import network.Network;
import clientGUI.RacingArenaClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import javax.swing.*;

public class Main {
    private static Network network;

    public static void main(String[] args) {
//        // connect to server
//        connectToServer();
//
//        network.send("HELLO! Testing 1, 2, 3, ...");

        createClientGUI();
    }

    private static void connectToServer() {
        network = new Network();
        network.connect();
    }

    private static void createClientGUI() {
        JFrame jFrame = new RacingArenaClient("Racing Arena");
        jFrame.setVisible(true);
    }
}
