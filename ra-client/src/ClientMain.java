import clientGUI.ClientGUI;
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
        JFrame jFrame = new ClientGUI("Racing Arena");
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
