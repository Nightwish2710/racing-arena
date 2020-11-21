import network.Network;
import network.NetworkConfig;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static Network network;

    public static void main(String[] args) {
        // connect to server
        connectToServer();

        while (true) {
            Scanner sc = new Scanner(System.in);
            String msg = sc.nextLine();
            System.out.println(Main.class.getSimpleName() + " says: "+ msg);
            network.send(NetworkConfig.CMD.CMD_TEST, msg);
            if(msg.equalsIgnoreCase("q")) {
                System.out.println(Main.class.getSimpleName() + " QUIT with "+ msg);
                break;
            }
        }
    }

    private static void connectToServer() {
        network = new Network();
        network.connect();
    }
}
