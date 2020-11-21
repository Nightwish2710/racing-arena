import network.Network;
import clientGUI.RacingArenaClient;
import network.NetworkConfig;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import javax.swing.*;
import java.util.Scanner;

public class Main {
    private static Network network;

    public static void main(String[] args) {
        // connect to server
        connectToServer();

//        network.send("HELLO! Testing 1, 2, 3, ...");

        createClientGUI();
        
        while (true) {
            Scanner sc = new Scanner(System.in);
            String msg = sc.nextLine();

            System.out.println(Main.class.getSimpleName() + " says: "+ msg);
            network.send(NetworkConfig.CMD.CMD_TEST, msg);

            if(msg.equalsIgnoreCase("q")) {
                System.out.println(Main.class.getSimpleName() + " QUIT with " + msg);
                break;
            }
        }
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
