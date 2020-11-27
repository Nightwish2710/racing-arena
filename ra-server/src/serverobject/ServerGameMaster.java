package serverobject;

public class ServerGameMaster {
    private int numberOfRacer;
    private int raceLength;

    private static ServerGameMaster serverRefereeObject = null;
    public static ServerGameMaster getInstance() {
        if (serverRefereeObject == null) {
            serverRefereeObject = new ServerGameMaster();
        }
        return  serverRefereeObject;
    }

    public ServerGameMaster() {
        serverRefereeObject = this;
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
