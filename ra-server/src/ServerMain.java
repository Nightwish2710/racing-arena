import serverGUI.ServerGUI;
import serverGUI.ServerGUIConfig;
import serverdatabase.ServerDBConfig;
import serverdatabase.ServerDBHelper;
import servernetwork.ServerNetwork;
import serverobject.ServerGameMaster;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;

public class ServerMain {
    private static ServerDBHelper serverDBHelper;
    private static ServerNetwork serverNetwork;
    private static ServerGameMaster serverGameMaster;

    public static void main(String args[]) throws IOException {
        initServerDB();
        initServerGameMaster();
        initServerGUI();
        initServerNetwork();
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
        JFrame jFrame = new ServerGUI(ServerGUIConfig.GAME_NAME);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();

        try {
            jFrame.setIconImage(ImageIO.read(new File("assets/dog-russel-grin-icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
