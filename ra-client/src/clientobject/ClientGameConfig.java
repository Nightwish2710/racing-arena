package clientobject;

public class ClientGameConfig {
    public static final int MAX_NUM_OF_INCORRECT = 3;

    public static final int FLAG_READY = 0;
    public static final int FLAG_NORMAL = 1;
    public static final int FLAG_FASTEST = 2;
    public static final int FLAG_TIMEOUT = 3;

    public static final String[] STATUS_STRING = {
            "Racer Ready",
            "Correct Answer",
            "Correct and Fastest Answer",
            "Timeout"
    };
}
