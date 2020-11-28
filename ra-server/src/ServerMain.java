import serverGUI.ServerConsoleOutput;
import serverGUI.ServerGUI;
import serverGUI.ServerGUIConfig;

import serverdatabase.ServerDBConfig;
import serverdatabase.ServerDBHelper;

import servernetwork.ServerNetwork;

import serverobject.ServerGameMaster;
import serverobject.ServerQuestion;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class ServerMain {
    private static ServerDBHelper serverDBHelper;
    private static ServerNetwork serverNetwork;

    private static ServerGameMaster serverGameMaster;
    private static JFrame serverGUI;

    public static void main(String args[]) throws IOException {
        System.setOut(new PrintStream(ServerConsoleOutput.getInstance()));

        initServerDB();
        initServerGameMaster();
        initServerGUI();
        initServerNetwork();

        System.out.println("HELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLO");
    }

    private static void initServerGameMaster() {
        serverGameMaster = new ServerGameMaster();
    }

    private static void initServerNetwork() {
        serverNetwork = new ServerNetwork();
    }

    private static void initServerDB() {
        serverDBHelper = new ServerDBHelper();
        serverDBHelper.exec(ServerDBConfig.CREATE_TABLE_RACER);
    }

    private static void initServerGUI() {
        serverGUI = new ServerGUI(ServerGUIConfig.GAME_NAME);

        serverGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverGUI.setResizable(false);
        serverGUI.pack();

        try {
            serverGUI.setIconImage(ImageIO.read(new File("assets/dog-russel-grin-icon.png")));
        } catch (IOException e) {
            System.err.println("Cannot set icon for Server UI");
            e.printStackTrace();
        }

        serverGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (serverDBHelper.isDBOpenning()) {
                    serverDBHelper.closeDB();
                }
                if (serverNetwork.isNetworkOpenning()) {
                    serverNetwork.closeNetwork();
                }
                super.windowClosing(e);
            }
        });

        serverGUI.setLocationRelativeTo(null);
        serverGUI.setVisible(true);
    }
}
