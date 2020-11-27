package serverobject;

public class ServerGameMaster {
    private int numOfRacers;
    private int raceLength;

    private static ServerGameMaster serverGameMaster = null;
    public static ServerGameMaster getInstance() {
        if (serverGameMaster == null) {
            serverGameMaster = new ServerGameMaster();
        }
        return serverGameMaster;
    }

    public ServerGameMaster() { serverGameMaster = this; }

    public int getNumOfRacers() {
        return this.numOfRacers;
    }
    public void setNumOfRacers(int numOfRacers) {
        this.numOfRacers = numOfRacers;
    }

    public int getRaceLength() {
        return this.raceLength;
    }
    public void setRaceLength(int raceLength) {
        this.raceLength = raceLength;
    }

    public ServerQuestion getQuestion() { return new ServerQuestion(); }
}
