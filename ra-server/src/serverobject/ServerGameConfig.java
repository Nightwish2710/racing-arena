package serverobject;

public class ServerGameConfig {
    // game configurations limitation
    public static final int MIN_NUM_OF_RACERS = 2;
    public static final int MAX_NUM_OF_RACERS = 10;
    public static final int MIN_RACE_LENGTH = 3;
    public static final int MAX_RACE_LENGTH = 26;

    // initial race value
    public static final int INIT_NUM_OF_RACERS = 6;
    public static final int INIT_RACE_LENGTH = 15;
    public static final int INIT_RACER_POSITION = 1;

    // number of incorrect answer that racer can make
    public static final int MAX_NUM_OF_INCORRECT = 3;

    // racer answer status
    public static final int FLAG_READY = 0;
    public static final int FLAG_NORMAL = 1;
    public static final int FLAG_FASTEST = 2;
    public static final int FLAG_TIMEOUT = 3;
}
