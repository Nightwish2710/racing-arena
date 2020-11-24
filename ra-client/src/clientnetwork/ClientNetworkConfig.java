package clientnetwork;

public class ClientNetworkConfig {
    public static String SERVER_HOST = "localhost";
    public static int SERVER_PORT = 3628;

    public static class CMD {
        public static final int DISCONNECT = -100;
        public static final int CMD_TEST = -1;
        public static final int CMD_LOGIN = -2;
        public static final int CMD_ANSWER = 1;
    }
}
