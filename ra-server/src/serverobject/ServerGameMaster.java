package serverobject;

import serverGUI.ServerGUI;
import serverdatabase.ServerDBConfig;
import serverdatabase.ServerDBHelper;
import serverdatamodel.response.SResAllRacersInfo;
import serverdatamodel.response.SResQuestion;
import servernetwork.ServerNetwork;
import servernetwork.ServerNetworkConfig;

import java.util.*;

public class ServerGameMaster {
    private int numOfRacers;
    private int raceLength;
    private HashMap<String, ServerRacerObject> sRacers;
    private HashMap<Integer, ServerQuestion> sQuestions;
    private Timer questionTimer;
    private boolean isEndgame;
    private int numOfRemainRacers;

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
        this.numOfRemainRacers = this.numOfRacers;
        this.raceLength = ServerGameConfig.INIT_RACE_LENGTH;
        this.isEndgame = false;
        serverGameMaster = this;
    }

    public int getNumOfRacers() { return this.numOfRacers; }
    public void setNumOfRacers(int numOfRacers) {
        this.numOfRacers = numOfRacers;
        this.numOfRemainRacers = numOfRacers;
    }

    public int getRaceLength() { return this.raceLength; }
    public void setRaceLength(int raceLength) { this.raceLength = raceLength; }

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
        return this.sRacers.get(rUsername);
    }

    public int getNumberOfPrevQuestions () {
        return this.sQuestions.size();
    }

    public void giveQuestion() {
        ServerQuestion serverQuestion = new ServerQuestion();
        serverQuestion.setStartingTimeOfQuestion(System.currentTimeMillis());

        // keep a record of this question in game master
        int sCurrentQuestionID = getNumberOfPrevQuestions() + 1;
        this.sQuestions.put(sCurrentQuestionID, serverQuestion);

        ServerGUI.getInstance().setGiveQuestionButton(true);

        // update question on UI
        ServerGUI.getInstance().setFirstNum(serverQuestion.getFirstNum());
        ServerGUI.getInstance().setSecondNum(serverQuestion.getSecondNum());
        ServerGUI.getInstance().setOperator(serverQuestion.getOperator());
        ServerGUI.getInstance().setAnswer(serverQuestion.getAnswer());

        // send packet to all clients
        SResQuestion sResQuestion = new SResQuestion(
                ServerNetworkConfig.CMD.CMD_QUESTION,
                sCurrentQuestionID,
                serverQuestion.getFirstNum(),
                serverQuestion.getOperator(),
                serverQuestion.getSecondNum(),
                serverQuestion.getStartingTimeOfQuestion()
        );
        ServerNetwork.getInstance().sendToAllClient(sResQuestion, -1, false);

        // start timer
        this._startTimer();
    }

    private void _startTimer() {
        ServerGUI.getInstance().setGiveQuestionButton(false);

        this.questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            int time = ServerGameConfig.MAX_TIMER_SEC;

            @Override
            public void run() {
                if (time > 0) {
                    time -= 1;
                    ServerGUI.getInstance().setUpdateTimer(time);
                }
                else {
                    finalEvaluateAfterAnAnswer();
                    ServerGUI.getInstance().setGiveQuestionButton(true);
                    questionTimer.cancel();
                }
            }
        }, 0, 1000);
    }

    public ServerQuestion getQuestion(int questionID) {
        return this.sQuestions.get(questionID);
    }

    public void finalEvaluateAfterAnAnswer() {
        long _minDeltaSAnsweringTime = Long.MAX_VALUE - 1;
        int lostPointsOfFuckedUpRacers = ServerGameConfig.GAME_BALANCE.GAIN_FASTEST;

        for (Map.Entry<String, ServerRacerObject> racerEntry : this.sRacers.entrySet()) {
            ServerRacerObject currRacer = racerEntry.getValue();

            // ignore previous eliminated or disconnected racer
            if (currRacer.getStatus() != ServerGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED &&
                    currRacer.getStatus() != ServerGameConfig.RACER_STATUS_FLAG.FLAG_QUIT) {
                // prepare shortest answering time, ignore wrong answers
                if (currRacer.getStatus() != ServerGameConfig.RACER_STATUS_FLAG.FLAG_WRONG &&
                        currRacer.getCurrDeltaSAnsweringTime() < _minDeltaSAnsweringTime) {
                    _minDeltaSAnsweringTime = currRacer.getCurrDeltaSAnsweringTime();
                }

                // timeout
                if (currRacer.getCurrDeltaSAnsweringTime() == ServerGameConfig.INIT_RACER_DELTA_ANSWERING_TIME) {
                    currRacer.updatePositionBy(ServerGameConfig.GAME_BALANCE.GAIN_TIMEOUT);
                    currRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_TIMEOUT);
                }

                //prepare total lose points of fucked up racers
                if (currRacer.getStatus() == ServerGameConfig.RACER_STATUS_FLAG.FLAG_TIMEOUT) {
                    lostPointsOfFuckedUpRacers += (-1) * ServerGameConfig.GAME_BALANCE.GAIN_TIMEOUT;
                }
                if (currRacer.getStatus() == ServerGameConfig.RACER_STATUS_FLAG.FLAG_WRONG) {
                    lostPointsOfFuckedUpRacers += (-1) * ServerGameConfig.GAME_BALANCE.GAIN_WRONG;
                }

                // eliminate
                if (currRacer.getNumOfWrong() == ServerGameConfig.GAME_BALANCE.MAX_NUM_OF_WRONG) {
                    currRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED);
                    numOfRemainRacers -= 1; // decrease number of remaining racers

                    ServerGUI.getInstance().strikeThroughEliminatedRacer(currRacer.getUsername()); // update table UI
                }
            }
        }

        // reward the fastest
        for (Map.Entry<String, ServerRacerObject> racerEntry : this.sRacers.entrySet()) {
            ServerRacerObject currRacer = racerEntry.getValue();

            // ignore eliminated or disconnected racer
            if (currRacer.getStatus() != ServerGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED &&
                    currRacer.getStatus() != ServerGameConfig.RACER_STATUS_FLAG.FLAG_QUIT) {
                // prepare shortest answering time
                if (currRacer.getStatus() != ServerGameConfig.RACER_STATUS_FLAG.FLAG_WRONG &&
                        currRacer.getCurrDeltaSAnsweringTime() <= _minDeltaSAnsweringTime) {
                    // the fastest
                    currRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_FASTEST);
                    currRacer.updatePositionBy(lostPointsOfFuckedUpRacers);

                    // the racer may also be the victor
                    if (currRacer.getPosition() >= raceLength) {
                        currRacer.updateNumOfVictoryBy(1);
                        currRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_VICTORY);

                        ServerGUI.getInstance().announceWinner(currRacer.getUsername()); // announce winner on UI

                        // write updated number of victory to database
                        String updateUser = "UPDATE " + ServerDBConfig.TABLE_RACER
                                + " SET " + ServerDBConfig.TABLE_RACER_victory + " = "+ currRacer.getNumOfVictory() + " WHERE "
                                + ServerDBConfig.TABLE_RACER_username + " = '" + currRacer.getUsername() + "'";
                        ServerDBHelper.getInstance().exec(updateUser);

                        isEndgame = true;
                        ServerGUI.getInstance().updateControllButtonToReplayButton();
                        ServerGUI.getInstance().changeStateOfControllButton();
                    }
                }
            }
        }

        // if no racers wins the race
        if (numOfRemainRacers <= 0) {
            isEndgame = true;
            ServerGUI.getInstance().changeStateOfControllButton();
            ServerGUI.getInstance().announceNoWinner();
        }

        int sCorrectAnswer = getQuestion(sQuestions.size()).getAnswer();

        // send to clients
        SResAllRacersInfo sResAllRacersInfo = new SResAllRacersInfo(
                ServerNetworkConfig.CMD.CMD_RESULT,
                sCorrectAnswer,
                ServerGameMaster.getInstance());
        ServerNetwork.getInstance().sendToAllClient(sResAllRacersInfo, -1, false);

        // update values on UI and reset flags
        for (Map.Entry<String, ServerRacerObject> racerEntry : this.sRacers.entrySet()) {
            ServerRacerObject currRacer = racerEntry.getValue();
            // update values on UI
            ServerGUI.getInstance().updateSRacerToUI(currRacer.getUsername(), currRacer.getGain(), currRacer.getStatus(), currRacer.getPosition());

            // reset flags, ignore eliminated or disconnected racer
            if (currRacer.getStatus() != ServerGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED &&
                    currRacer.getStatus() != ServerGameConfig.RACER_STATUS_FLAG.FLAG_QUIT) {
                currRacer.resetRacerForNewQuestion();
            }
        }
    }

    public void replay() {
        isEndgame = false;

        numOfRemainRacers = numOfRacers;

        sQuestions.clear();
        sQuestions = new HashMap<>();

        resetAllRacersForNewMatch(); // reset table
        ServerGUI.getInstance().resetUIForReplay(); // reset UI

        SResAllRacersInfo sResAllRacersInfo = new SResAllRacersInfo(ServerNetworkConfig.CMD.CMD_REPLAY, Integer.MAX_VALUE, this);
        ServerNetwork.getInstance().sendToAllClient(sResAllRacersInfo, -1, false);
    }

    private void resetAllRacersForNewMatch() {
        for (Map.Entry<String, ServerRacerObject> racerEntry : this.sRacers.entrySet()) {
            ServerRacerObject currRacer = racerEntry.getValue();
            currRacer.resetRacerForNewMatch();

            // update values on UI
            ServerGUI.getInstance().updateSRacerToUI(currRacer.getUsername(), currRacer.getGain(), currRacer.getStatus(), currRacer.getPosition());
            ServerGUI.getInstance().updateSRacerAnswerToUI(currRacer.getUsername(), Integer.MAX_VALUE);
            ServerGUI.getInstance().renewRacerNickname(currRacer);
        }
    }

    public void receiveAnswerFromARacer(String racerName, int racerAnswer) {
        ServerGUI.getInstance().updateSRacerAnswerToUI(racerName, racerAnswer);
    }
}
