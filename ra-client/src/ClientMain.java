import clientGUI.ClientGUI;
import clientGUI.ClientGUIConfig;
import clientnetwork.ClientNetwork;
import clientobject.ClientGameMaster;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class ClientMain {
    private static ClientNetwork network;
    private static ClientGameMaster clientGameMaster;
    private static JFrame clientGUI;

    public static void main(String[] args) {
        // connect to server
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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                clientGUI = new ClientGUI(ClientGUIConfig.GAME_NAME);
                clientGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                try {
                    clientGUI.setIconImage(ImageIO.read(new File("assets/dog-sharpei-icon.png")));
                } catch (IOException e) {
                    System.err.println("Cannot set icon for Client UI");
                    e.printStackTrace();
                }

                clientGUI.pack();
                clientGUI.setLocationRelativeTo(null);
//                clientGUI.setVisible(true);

                clientGUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        if (network.isConnected()) {
                            network.disconnect();
                            System.out.println(ClientMain.class.getSimpleName() + ": disconnect from server");
                        }
                        super.windowClosed(e);
                    }
                });
            }
        });
    }
}
