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
    public static void main(String[] args) {

//        java.net.URL url = ClassLoader.getSystemResource("com/xyz/assets/dog-sharpei-icon.png");

        // connect to server
        connectToServer();

        initClientGUI();

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
                JFrame jFrame = new ClientGUI(ClientGUIConfig.GAME_NAME);
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.pack();

                try {
                    jFrame.setIconImage(ImageIO.read(new File("assets/dog-sharpei-icon.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                jFrame.setLocationRelativeTo(null);
                jFrame.setVisible(true);

                jFrame.addWindowListener(new WindowAdapter() {
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
