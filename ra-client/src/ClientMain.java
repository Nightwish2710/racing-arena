import clientGUI.ClientGUI;
import clientGUI.ClientGUIConfig;
import clientnetwork.ClientNetwork;
import clientnetwork.ClientNetworkConfig;
import clientdatamodel.CDAccount;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientMain {
    private static ClientNetwork network;

    public static void main(String[] args) {
        // connect to server
        connectToServer();

        createClientGUI();

        // testing connection
//        while (true) {
//            Scanner sc = new Scanner(System.in);
//            String msg = sc.nextLine();
//
//            //System.out.println(ClientMain.class.getSimpleName() + " says: " + msg);
//            network.send(ClientNetworkConfig.CMD.CMD_TEST, ClientGUI.getUserName());
//
//            if(msg.equalsIgnoreCase("q")) {
//                System.out.println(ClientMain.class.getSimpleName() + " QUIT with " +  msg);
//                break;
//            }
//        }
        // Sample login
        String username = "Anh Thu";
        String password = "AnhThu";
        CDAccount cdLogin = new CDAccount(username, password);
        network.send(ClientNetworkConfig.CMD.CMD_LOGIN, cdLogin);
    }

    private static void connectToServer() {
        network = new ClientNetwork();
        network.connect();
    }

    private static void createClientGUI() {
        JFrame jFrame = new ClientGUI(ClientGUIConfig.GAME_NAME);
        jFrame.setVisible(true);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                network.disconnect();
                System.out.println(ClientMain.class.getSimpleName() + ": disconnect from server");

                super.windowClosed(e);
            }
        });
    }
}
