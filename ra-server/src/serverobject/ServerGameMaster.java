package serverobject;

public class ServerGameMaster {
    private int numberOfRacer;
    private int raceLength;

    private static ServerGameMaster serverGameMaster = null;
    public static ServerGameMaster getInstance() {
        if (serverGameMaster == null) {
            serverGameMaster = new ServerGameMaster();
        }
        return serverGameMaster;
    }

    public ServerGameMaster() {
        serverGameMaster = this;
    }

    public int getNumberOfRacer() {
        return numberOfRacer;
    }

    public void setNumberOfRacer(int numberOfRacer) {
        this.numberOfRacer = numberOfRacer;
    }

    public int getRaceLength() {
        return raceLength;
    }

    public void setRaceLength(int raceLength) {
        this.raceLength = raceLength;
    }
}
