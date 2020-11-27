import clientGUI.ClientGUI;
import clientGUI.ClientGUIConfig;
import clientnetwork.ClientNetwork;
import clientnetwork.ClientNetworkConfig;
import clientdatamodel.CDAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientMain {
    private static ClientNetwork network;

    public static void main(String[] args) {
        // connect to server
        connectToServer();

        createClientGUI();
    }

    private static void connectToServer() {
        network = new ClientNetwork();
        network.connect();
    }

    private static void createClientGUI() {
//        JFrame jFrame = new ClientGUI(ClientGUIConfig.GAME_NAME);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame jFrame = new ClientGUI(ClientGUIConfig.GAME_NAME);
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.pack();

                jFrame.setLocationRelativeTo(null);
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
        });

//        jFrame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                network.disconnect();
//                System.out.println(ClientMain.class.getSimpleName() + ": disconnect from server");
//
//                super.windowClosed(e);
//            }
//        });

//        jFrame.pack();
//
//        jFrame.setLocationRelativeTo(null);
//        jFrame.setVisible(true);

    }
}
