import client_network.ClientNetwork;
import client_network.ClientNetworkConfig;

import java.util.Scanner;

public class ClientMain {
    private static ClientNetwork network;

    public static void main(String[] args) {
        // connect to server
        connectToServer();

        while (true) {
            Scanner sc = new Scanner(System.in);
            String msg = sc.nextLine();
            System.out.println(ClientMain.class.getSimpleName() + " says: "+ msg);
            network.send(ClientNetworkConfig.CMD.CMD_TEST, msg);
            if(msg.equalsIgnoreCase("q")) {
                System.out.println(ClientMain.class.getSimpleName() + " QUIT with "+ msg);
                break;
            }
        }
    }

    private static void connectToServer() {
        network = new ClientNetwork();
        network.connect();
    }
}
