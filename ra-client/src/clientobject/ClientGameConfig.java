package clientobject;

public class ClientGameConfig {
    // game configurations limitation
    public static final int MIN_NUM_OF_RACERS = 2;
    public static final int MAX_NUM_OF_RACERS = 10;
    public static final int MIN_RACE_LENGTH = 3;
    public static final int MAX_RACE_LENGTH = 26;
    public static final int MAX_TIMER = 10;

    // number of incorrect answer that racer can make
    public static final int MAX_NUM_OF_INCORRECT = 3;

    // racer status
    public static class RACER_STATUS_FLAG {
        public static final int FLAG_READY = 0;
        public static final int FLAG_NORMAL = 1;
        public static final int FLAG_FASTEST = 2;
        public static final int FLAG_TIMEOUT = 3;
        public static final int FLAG_ELIMINATED = 4;
        public static final int FLAG_QUIT = 5;
    }

    public static final String[] STATUS_STRING = {
            "ClientRacer Ready",
            "Correct Answer",
            "Correct and Fastest Answer",
            "Timeout",
            "Eliminated",
            "Raged Quit"
    };

    // type of racer object info flag
    public static class RACER_OBJECT_INFO_TYPE_FLAG {
        public static final int TYPE_VICTORY = 0;
        public static final int TYPE_POSITION = 1;
        public static final int TYPE_STATUS = 2;
    }
}
