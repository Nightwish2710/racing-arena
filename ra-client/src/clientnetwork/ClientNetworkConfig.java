package clientnetwork;

public class ClientNetworkConfig {
    public static String SERVER_HOST = "localhost";
    public static int SERVER_PORT = 3628;

    // racer actions
    public static class CMD {
        public static final int DISCONNECT = -100;
        public static final int CMD_LOGIN = 0;
        public static final int CMD_INFO = 1;
        public static final int CMD_QUESTION = 2;
        public static final int CMD_ANSWER = 3;
        public static final int CMD_RESULT = 4;
        public static final int CMD_REPLAY = 5;
    }

    public static class LOGIN_FLAG {
        public static final int SUCCESS = 0;
        public static final int USERNAME_TAKEN = 1;
        public static final int NO_MORE_SLOTS = 2;
        public static final int DUPLICATED_LOGIN = 3;
        public static final int ERROR = 4;
    }

    public static class INFO_TYPE_FLAG {
        public static final int TYPE_NOTICE_NEW_OPPONENT = 0;
        public static final int TYPE_NOTICE_UPDATE_OPPONENT = 1;
    }
}
