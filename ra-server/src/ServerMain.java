import serverGUI.ServerGUI;
import serverGUI.ServerGUIConfig;
import serverdatabase.ServerDBConfig;
import serverdatabase.ServerDBHelper;
import servernetwork.ServerNetwork;
import serverobject.ServerRefereeObject;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class ServerMain {
    private static ServerDBHelper serverDBHelper;
    private static ServerNetwork serverNetwork;
    private static ServerRefereeObject serverReferee;

    public static void main(String args[]) throws IOException {
        initServerDB();
        initServerReferee();
        initServerGUI();
        initServerNetwork();
    }

    private static void initServerReferee() {
        serverReferee = new ServerRefereeObject();
    }

    private static void initServerNetwork() {
        serverNetwork = new ServerNetwork();
    }

    private static void initServerDB() {
        serverDBHelper = new ServerDBHelper();
        serverDBHelper.exec(ServerDBConfig.CREATE_TABLE_USER);
        System.out.println(ServerMain.class.getSimpleName() + "DB create USER_ACCOUNT table");
    }

    private static void initServerGUI() {
        JFrame jFrame = new ServerGUI(ServerGUIConfig.GAME_NAME);

        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
