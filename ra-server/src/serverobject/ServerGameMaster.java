package serverobject;

import servernetwork.ServerCSocketThread;

import java.util.HashMap;
import java.util.Map;

public class ServerGameMaster {
    private int numOfRacers;
    private int raceLength;
    private HashMap<Integer, ServerRacerObject> sRacers;

    // singleton
    private static ServerGameMaster serverGameMaster = null;
    public static ServerGameMaster getInstance() {
        if (serverGameMaster == null) {
            serverGameMaster = new ServerGameMaster();
        }
        return serverGameMaster;
    }

    public ServerGameMaster() {
        this.sRacers = new HashMap<>();
        this.numOfRacers = ServerGameConfig.INIT_NUM_OF_RACERS;
        serverGameMaster = this;
    }

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

    public void addSRacer (ServerRacerObject sRacer) {
        sRacers.put(sRacer.getRacerID(), sRacer);
    }

    public int getNextRacerID() {
        return sRacers.size() + 1;
    }

    public HashMap<Integer, ServerRacerObject> getsRacers() {
        return sRacers;
    }

    public int getCurrentNumOfRacers() {
        return sRacers.size();
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
                    capacity += Integer.BYTES; // hold rUsername length
                    capacity += rUsername.length();

                    // int position
                    capacity += Integer.BYTES;

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
                capacity += Integer.BYTES; // hold rUsername length
                capacity += rUsername.length();

                // int position
                capacity += Integer.BYTES;

                // int rStatus;
                capacity += Integer.BYTES;
            }
        }
        return capacity;
    }

    public int getSizeInBytesOfRacer (int racerID) {
        int capacity = 0;
        for (Map.Entry<Integer, ServerRacerObject> entry : this.sRacers.entrySet()) {
            if (entry.getKey() == racerID) {
                ServerRacerObject racerObject = entry.getValue();
                // int rID;
                capacity += Integer.BYTES;

                String rUsername = racerObject.getUsername();
                capacity += Integer.BYTES; // hold rUsername length
                capacity += rUsername.length();

                // int position
                capacity += Integer.BYTES;

                // int rStatus;
                capacity += Integer.BYTES;

                break;
            }
        }
        return capacity;
    }

    public ServerRacerObject getRacerInfo(int racerID) {
        return sRacers.get(racerID);
    }

    public ServerQuestion getQuestion() { return new ServerQuestion(); }
}
