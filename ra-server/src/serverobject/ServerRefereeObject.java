package serverobject;

public class ServerRefereeObject {
    private int numberOfRacer;
    private int raceLength;

    private static ServerRefereeObject serverRefereeObject = null;
    public static ServerRefereeObject getInstance() {
        if (serverRefereeObject == null) {
            serverRefereeObject = new ServerRefereeObject();
        }
        return  serverRefereeObject;
    }

    public ServerRefereeObject() {
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
