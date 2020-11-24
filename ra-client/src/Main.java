import network.Network;
import clientGUI.ClientGUI;
import network.NetworkConfig;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    private static Network network;

    public static void main(String[] args) {
        connectToServer();

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
        JFrame jFrame = new ClientGUI("Racing Arena");

        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
