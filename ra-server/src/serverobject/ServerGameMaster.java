package serverobject;

import serverGUI.ServerGUI;
import serverdatamodel.response.SResAllRacersInfo;
import serverdatamodel.response.SResQuestion;
import servernetwork.ServerNetwork;
import servernetwork.ServerNetworkConfig;

import java.util.ArrayList;
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

    public ServerRacerObject getRacerByUsername(String rUsername) {
        return this.sRacers.get(rUsername);
    }

    public ServerRacerObject getRacerInfo(String rUsername) {
        return sRacers.get(rUsername);
    }

    public int getNumberOfPrevQuestions () {
        return this.sQuestions.size();
    }

    public void giveQuestion() {
        ServerQuestion serverQuestion = new ServerQuestion();
        serverQuestion.setStartingTimeOfQuestion(System.currentTimeMillis());

        // Keep a record of this question in game master
        int sCurrentQuestionID = getNumberOfPrevQuestions() + 1;
        this.sQuestions.put(sCurrentQuestionID, serverQuestion);

        // Update question on UI
        ServerGUI.getInstance().setFirstNum(serverQuestion.getFirstNum());
        ServerGUI.getInstance().setSecondNum(serverQuestion.getSecondNum());
        ServerGUI.getInstance().setOperator(serverQuestion.getOperator());

        // Send packet to all clients
        SResQuestion sResQuestion = new SResQuestion(
                ServerNetworkConfig.CMD.CMD_QUESTION,
                sCurrentQuestionID,
                serverQuestion.getFirstNum(),
                serverQuestion.getOperator(),
                serverQuestion.getSecondNum(),
                serverQuestion.getStartingTimeOfQuestion()
        );
        ServerNetwork.getInstance().sendToAllClient(sResQuestion, -1, false);
    }

    public ServerQuestion getQuestion(int questionID) {
        return this.sQuestions.get(questionID);
    }

    public void finalEvaluateAfterAnAnswer() {
        long _minDeltaSAansweringTime = Long.MAX_VALUE;
        int losePointsOfFuckedUpRacers = 0;

        for (Map.Entry<String, ServerRacerObject> racerEntry : this.sRacers.entrySet()) {
            ServerRacerObject currRacer = racerEntry.getValue();
            // ignore previous eliminated or disconnected racer
            if (currRacer.getStatus() == ServerGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED || currRacer.getStatus() == ServerGameConfig.RACER_STATUS_FLAG.FLAG_QUIT) {
                // prepare shortest answering time
                if (currRacer.getCurrDeltaSAnsweringTime() < _minDeltaSAansweringTime) {
                    _minDeltaSAansweringTime = currRacer.getCurrDeltaSAnsweringTime();
                }

                //prepare total lose points of fucked up racers
                if (currRacer.getGain() < 0) {
                    losePointsOfFuckedUpRacers -= currRacer.getGain();
                }

                // eliminate
                if (currRacer.getNumOfWrong() == ServerGameConfig.GAME_BALANCE.MAX_NUM_OF_WRONG) {
                    currRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED);
                }
            }
        }

        // reward the fastest
        for (Map.Entry<String, ServerRacerObject> racerEntry : this.sRacers.entrySet()) {
            ServerRacerObject currRacer = racerEntry.getValue();
            // ignore eliminated or disconnected racer
            if (currRacer.getStatus() == ServerGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED || currRacer.getStatus() == ServerGameConfig.RACER_STATUS_FLAG.FLAG_QUIT) {
                // prepare shortest answering time
                if (currRacer.getCurrDeltaSAnsweringTime() == _minDeltaSAansweringTime) {
                    // the fastest
                    currRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_FASTEST);
                    currRacer.updatePositionBy(losePointsOfFuckedUpRacers);
                }
            }
        }

        // send to clients
        SResAllRacersInfo sResAllRacersInfo = new SResAllRacersInfo(
                ServerNetworkConfig.CMD.CMD_RESULT,
                ServerNetworkConfig.INFO_TYPE_FLAG.TYPE_NOTICE_UPDATE_ALL_RACERS,
                ServerGameMaster.getInstance());
        ServerNetwork.getInstance().sendToAllClient(sResAllRacersInfo, -1, false);
    }
}
