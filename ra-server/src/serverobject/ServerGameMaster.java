package serverobject;

import serverGUI.ServerGUI;
import serverdatamodel.response.SResQuestion;
import servernetwork.ServerNetwork;
import servernetwork.ServerNetworkConfig;

import java.util.HashMap;
import java.util.Map;

public class ServerGameMaster {
    private int numOfRacers;
    private int raceLength;
    private HashMap<String, ServerRacerObject> sRacers;
    private HashMap<Integer, ServerQuestion> sQuestions;

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
        this.sQuestions = new HashMap<>();
        this.numOfRacers = ServerGameConfig.INIT_NUM_OF_RACERS;
        serverGameMaster = this;
    }

    public int getNumOfRacers() { return this.numOfRacers; }
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

        // Show this new racer on UI (increase number of joining racers and add to statistics table)
        ServerGUI.getInstance().updateNumOfPplJoiningValue(this.getCurrentNumOfRacers());
        ServerGUI.getInstance().addSRacerToUI(sRacer.getUsername(), sRacer.getGain(), sRacer.getStatus(), sRacer.getPosition());
    }
    public void removeRacer(String racerName) {
        sRacers.remove(racerName);
        ServerGUI.getInstance().updateNumOfPplJoiningValue(this.getCurrentNumOfRacers());
        ServerGUI.getInstance().removeSRacerFromUI(racerName);
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

    public void updateRacerInfo (String rUsername, int infoType, Object info) {
        switch (infoType) {
            case ServerGameConfig.RACER_OBJECT_INFO_TYPE_FLAG.TYPE_VICTORY:
                this.sRacers.get(rUsername).setNumOfVictory((int) info);
                break;
            case ServerGameConfig.RACER_OBJECT_INFO_TYPE_FLAG.TYPE_POSITION:
                this.sRacers.get(rUsername).setPosition((int) info);
                break;
            case ServerGameConfig.RACER_OBJECT_INFO_TYPE_FLAG.TYPE_STATUS:
                this.sRacers.get(rUsername).setStatus((int) info);
                break;
            default:
                break;
        }
    }
    public ServerRacerObject getRacerInfo(String rUsername) {
        return sRacers.get(rUsername);
    }

    public int getNumberOfPrevQuestions () {
        return this.sQuestions.size();
    }

    public void giveQuestion() {
        ServerQuestion serverQuestion = new ServerQuestion();
        int sCurrentQuestionID = getNumberOfPrevQuestions() + 1;
        this.sQuestions.put(sCurrentQuestionID, serverQuestion);

        // Show Num1, Op, Num2 on UI

        // Send packet to all clients
        SResQuestion sResQuestion = new SResQuestion(ServerNetworkConfig.CMD.CMD_QUESTION, sCurrentQuestionID, serverQuestion.getFirstNum(), serverQuestion.getOperator(), serverQuestion.getSecondNum());
        ServerNetwork.getInstance().sendToAllClient(sResQuestion, -1, false);

        // Start timer
    }
}
