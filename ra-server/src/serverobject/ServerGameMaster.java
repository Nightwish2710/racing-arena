package serverobject;

import servernetwork.ServerCSocketThread;

import java.util.HashMap;
import java.util.Map;

public class ServerGameMaster {
    private int numberOfRacer;
    private int raceLength;
    private HashMap<Integer, ServerRacerObject> sRacers;

    private static ServerGameMaster serverGameMaster = null;
    public static ServerGameMaster getInstance() {
        if (serverGameMaster == null) {
            serverGameMaster = new ServerGameMaster();
        }
        return serverGameMaster;
    }

    public ServerGameMaster() {
        sRacers = new HashMap<>();
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

    public void addSRacer (ServerRacerObject sRacer) {
        sRacers.put(sRacer.getRacerID(), sRacer);
    }

    public int getNextRacerID() {
        return sRacers.size() + 1;
    }

    public HashMap<Integer, ServerRacerObject> getsRacers() {
        return sRacers;
    }

    public int getSizeInBytes(boolean ignore, int clientID) {
        int capacity = 0;
        if (ignore) {
            for (Map.Entry<Integer, ServerRacerObject> entry : this.sRacers.entrySet()) {
                if (entry.getKey() != clientID) {
                    ServerRacerObject racerObject = entry.getValue();
                    // int rID;
                    capacity += Integer.BYTES;

                    String rUsername = racerObject.getUsername();
                    capacity += rUsername.length();

                    String rPassword = racerObject.getPassword();
                    capacity += rPassword.length();

                    // int rStatus;
                    capacity += Integer.BYTES;
                }
            }
        } else {
            for (Map.Entry<Integer, ServerRacerObject> entry : this.sRacers.entrySet()) {
                ServerRacerObject racerObject = entry.getValue();
                // int rID;
                capacity += Integer.BYTES;

                String rUsername = racerObject.getUsername();
                capacity += rUsername.length();

                String rPassword = racerObject.getPassword();
                capacity += rPassword.length();

                // int rStatus;
                capacity += Integer.BYTES;
            }
        }
        return capacity;
    }
}
