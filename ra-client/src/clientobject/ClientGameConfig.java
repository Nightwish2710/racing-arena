package clientobject;

public class ClientGameConfig {
    // number of incorrect answer that racer can make
    public static final int MAX_NUM_OF_INCORRECT = 3;

    // racer answer status
    public static final int FLAG_READY = 0;
    public static final int FLAG_NORMAL = 1;
    public static final int FLAG_FASTEST = 2;
    public static final int FLAG_TIMEOUT = 3;

    public static final String[] STATUS_STRING = {
            "ClientRacer Ready",
            "Correct Answer",
            "Correct and Fastest Answer",
            "Timeout"
    };
}
