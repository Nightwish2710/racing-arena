import clientGUI.ClientConsoleOutput;
import clientGUI.ClientGUI;
import clientGUI.ClientGUIConfig;
import clientnetwork.ClientNetwork;
import clientobject.ClientGameMaster;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientMain {
    private static ClientNetwork network;

    private static ClientGameMaster clientGameMaster;
    private static JFrame clientGUI;

    public static void main(String[] args) {
//        System.setOut(new PrintStream(ClientConsoleOutput.getInstance()));

        initClientGUI();
        connectToServer();
        initClientGameMaster();
    }

    private static void initClientGameMaster() {
        clientGameMaster = new ClientGameMaster();
    }

    private static void connectToServer() {
        network = new ClientNetwork();
        network.connect();
    }

    private static void initClientGUI() {
        clientGUI = new ClientGUI(ClientGUIConfig.GAME_NAME);
        clientGUI.pack();

        clientGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (network.isConnected()) {
                    network.disconnect();
                    System.out.println(ClientMain.class.getSimpleName() + ": disconnect from server");
                }
                super.windowClosed(e);
                System.exit(-1);
            }
        });

        clientGUI.setLocationRelativeTo(null);
    }
}
