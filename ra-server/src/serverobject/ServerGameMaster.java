package serverobject;

import java.util.HashMap;
import java.util.Map;

public class ServerGameMaster {
    private int numOfRacers;
    private int raceLength;
    private HashMap<String, ServerRacerObject> sRacers;

    // Singleton
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
        sRacers.put(sRacer.getUsername(), sRacer);
    }

    public HashMap<String, ServerRacerObject> getsRacers() {
        return sRacers;
    }

    public int getCurrentNumOfRacers() {
        return sRacers.size();
    }

    public int getSizeInBytes(boolean ignore, String cUsername) {
        int capacity = 0;

        if (ignore) {
            for (Map.Entry<String, ServerRacerObject> entry : this.sRacers.entrySet()) {
                if (!entry.getKey().equals(cUsername)) {
                    ServerRacerObject racerObject = entry.getValue();

                    // string rUsername
                    String rUsername = racerObject.getUsername();
                    capacity += Integer.BYTES; // hold rUsername length
                    capacity += rUsername.length();

                    // int position
                    capacity += Integer.BYTES;

                    // int rStatus
                    capacity += Integer.BYTES;
                }
            }
        }
        else {
            for (Map.Entry<String, ServerRacerObject> entry : this.sRacers.entrySet()) {
                ServerRacerObject racerObject = entry.getValue();

                // string rUsername
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

    public int getSizeInBytesOfRacer (String cUsername) {
        int capacity = 0;
        for (Map.Entry<String, ServerRacerObject> entry : this.sRacers.entrySet()) {
            if (entry.getKey().equals(cUsername)) {
                ServerRacerObject racerObject = entry.getValue();

                // string rUsername
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

    public ServerRacerObject getRacerInfo(String rUsername) {
        return sRacers.get(rUsername);
    }

    public ServerQuestion getQuestion() { return new ServerQuestion(); }
}
