package serverobject;

public class ServerGameConfig {
    // game configurations limitation
    public static final int MIN_NUM_OF_RACERS = 2;
    public static final int MAX_NUM_OF_RACERS = 10;
    public static final int MIN_RACE_LENGTH = 3;
    public static final int MAX_RACE_LENGTH = 26;
    public static final int MAX_TIMER_SEC = 10;
    public static final long MAX_TIMER_MILIS = MAX_TIMER_SEC * 1000;

    // initial race value
    public static final int INIT_NUM_OF_RACERS = 6;
    public static final int INIT_RACE_LENGTH = 15;
    public static final int INIT_RACER_POSITION = 1;
    public static final long INIT_RACER_DELTA_ANSWERING_TIME = Long.MAX_VALUE;

    // number of incorrect answer that racer can make
    public static final int MAX_NUM_OF_INCORRECT = 3;

    // question configurations
    // public static final int MAX_NUMBER = 10000, MIN_NUMBER = -10000;
    public static final int MAX_NUMBER = 10, MIN_NUMBER = -10;
    public static class OPERATOR_FLAG {
        public static final int ADD_OP = 0;
        public static final int MINUS_OP = 1;
        public static final int MULTIPLY_OP = 2;
        public static final int DIVIDE_OP = 3;
        public static final int MODULA_OP = 4;
    }
    public static final String[] OPERATORS = {"+", "-", "*", "/ ", "%"};

    // racer status
    public static class RACER_STATUS_FLAG {
        public static final int FLAG_READY = 0;
        public static final int FLAG_NORMAL = 1;
        public static final int FLAG_FASTEST = 2;
        public static final int FLAG_WRONG = 3;
        public static final int FLAG_TIMEOUT = 4;
        public static final int FLAG_ELIMINATED = 5;
        public static final int FLAG_QUIT = 6;
    }
    public static final String[] STATUS_STRING = {
            "Racer Ready",
            "Correct Answer",
            "Correct & Fastest Answer",
            "Timeout",
            "Eliminated",
            "Raged Quit"
    };

    public static class GAME_BALANCE {
        public static final int GAIN_NORMAL = 1;
        public static final int GAIN_WRONG = -1;
        public static final int GAIN_TIMEOUT = -1;
        public static final int GAIN_FASTEST = 1;
        public static final int MAX_NUM_OF_WRONG = 3;
    }
}
