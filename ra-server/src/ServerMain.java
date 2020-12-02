import serverGUI.ServerGUI;
import serverGUI.ServerGUIConfig;

import serverdatabase.ServerDBConfig;
import serverdatabase.ServerDBHelper;

import servernetwork.ServerNetwork;

import serverobject.ServerGameMaster;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class ServerMain {
    private static ServerDBHelper serverDBHelper;
    private static ServerNetwork serverNetwork;

    private static ServerGameMaster serverGameMaster;
    private static JFrame serverGUI;

    public static void main(String args[]) {
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
        serverGUI = new ServerGUI(ServerGUIConfig.GAME_NAME);
        serverGUI.pack();

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
