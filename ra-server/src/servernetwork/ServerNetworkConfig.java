package servernetwork;

public class ServerNetworkConfig {
    public static final int SERVER_PORT = 3628;

    public static class CMD {
        public static final int DISCONNECT = -100;
        public static int CMD_TEST = -1;
        public static int CMD_LOGIN = -2;
        public static int CMD_ANSWER = 1;
    }
}
