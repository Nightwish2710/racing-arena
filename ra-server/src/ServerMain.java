import serverdatabase.ServerDBConfig;
import serverdatabase.ServerDBHelper;
import servernetwork.ServerNetwork;

import java.io.*;

public class ServerMain {
    private static ServerDBHelper serverDBHelper;
    private static ServerNetwork serverNetwork;

    public static void main(String args[]) throws IOException {
        initServerDB();
        initServerNetwork();
    }

    private static void initServerNetwork() {
        serverNetwork = new ServerNetwork();
        serverNetwork.openServerSocket();
    }

    private static void initServerDB() {
        serverDBHelper = new ServerDBHelper();
        serverDBHelper.exec(ServerDBConfig.CREATE_TABLE_USER);
        System.out.println(ServerMain.class.getSimpleName() + "DB create USER_ACCOUNT table");
    }

}
