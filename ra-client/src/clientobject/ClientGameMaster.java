package clientobject;

import clientGUI.ClientGUI;
import clientdatamodel.send.CSenAnswer;
import clientnetwork.ClientNetwork;
import clientnetwork.ClientNetworkConfig;

import java.util.HashMap;
import java.util.Map;

public class ClientGameMaster {
    private int numOfRacers;
    private ClientRacer cRacer;
    private HashMap<String, ClientPlayer> cOpponents; // <username, opponentObject>
    private ClientQuestion currentQuestion;

    // Singleton
    private static ClientGameMaster clientGameMaster = null;
    public static ClientGameMaster getInstance() {
        if (clientGameMaster == null) {
            clientGameMaster = new ClientGameMaster();
        }
        return clientGameMaster;
    }

    public ClientGameMaster() {
        this.cRacer = new ClientRacer("", 0, 0, -1, "");
        this.numOfRacers = 0;
        this.cOpponents = null;
        this.currentQuestion = null;
        clientGameMaster = this;
    }

    public ClientRacer getCRacer() { return this.cRacer; }

    public int getNumOfRacers() { return this.numOfRacers; }
    public void setNumOfRacers(int numOfRacers) { this.numOfRacers = numOfRacers; }

    public int getCurrentNumOfRacers() { return this.cOpponents.size() + 1; }

    public void setInitCOpponents(HashMap<String, ClientPlayer> cOpponents) {
        this.cOpponents = cOpponents;

        ClientGUI.getInstance().initOpponentBarWhenReceiveNumOfPplJoinning();

        int order = 2;
        for (Map.Entry<String, ClientPlayer> opps : this.cOpponents.entrySet()) {
            System.out.println("PREV OPPOs: " + opps.getKey());

            ClientGUI.getInstance().updateOpponentProgress(order, opps.getValue());
            order += 1;
        }
    }

    public void addNewOpponent(ClientPlayer cNewOpponent) {
        System.out.println("NEW OPPOs: " + cNewOpponent.getNickname());
        this.cOpponents.put(cNewOpponent.getNickname(), cNewOpponent);
        int order = this.getCurrentNumOfRacers();
        ClientGUI.getInstance().updateOpponentProgress(order, cNewOpponent);
    }

    public void updateAnOpponent(ClientPlayer cOpponent) {
        this.cOpponents.put(cOpponent.getNickname(), cOpponent);
        // might call UI here

        System.out.println(getClass().getSimpleName() + ": " + cOpponent.getNickname() + " status: " + this.cOpponents.get(cOpponent.getNickname()).getStatusFlag());
    }

    public void confirmRacerPostLogin(int numOfVictory) {
        this.cRacer.setNumOfVictory(numOfVictory);

        ClientGUI.getInstance().setNickname(this.cRacer.getNickname());
        ClientGUI.getInstance().setNumOfVictory(numOfVictory); // update victory count on UI
    }

    public ClientQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(ClientQuestion currentQuestion) {
        this.currentQuestion = currentQuestion;

        ClientGUI.getInstance().setFirstNum(currentQuestion.getFirstNum());
        ClientGUI.getInstance().setOperator(currentQuestion.getOperator());
        ClientGUI.getInstance().setSecondNum(currentQuestion.getSecondNum());

        try {
            ClientGUI.getInstance().startAnswering();
            ClientGUI.getInstance().stopAnswering();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void giveAnswer(int cAnswer) {
        CSenAnswer cSenAnswer = new CSenAnswer(
                ClientNetworkConfig.CMD.CMD_ANSWER,
                this.currentQuestion.getQuestionId(),
                cAnswer,
                System.currentTimeMillis() - this.currentQuestion.getTimeOffset()
        );
        ClientNetwork.getInstance().send(cSenAnswer);
    }

    public void updateThisRacer(ClientRacer updatedThisRacer) {
        // new position, new status on UI
        this.cRacer = updatedThisRacer;
        ClientGUI.getInstance().updateYouPoint(this.cRacer.getPosition());
        ClientGUI.getInstance().setUpdateStatus(ClientGameConfig.STATUS_STRING[this.cRacer.getStatusFlag()]);

        String gainStr = this.cRacer.getGain() >= 0 ? ("+"+String.valueOf(this.cRacer.getGain())) : String.valueOf(this.cRacer.getGain());
        ClientGUI.getInstance().setUpdateExtraStatus("Gain: " + gainStr + " ");

        switch (this.cRacer.getStatusFlag()) {
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_WRONG:
                this.cRacer.updateNumOfIncorrectBy(1);
                ClientGUI.getInstance().setUpdateExtraStatus("Wrong: " + this.cRacer.getNumOfIncorrect() + " times ");
                break;
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_TIMEOUT:
                break;
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED:
                // block answer box and send-answer button

                ClientGUI.getInstance().setUpdateExtraStatus("You were ejected :>> ");
                break;
            default:
                break;
        }
    }

    public void updateAllOpponents(HashMap<String, ClientPlayer> allRacers) {
        for (Map.Entry<String, ClientPlayer> racer : allRacers.entrySet()) {
            ClientPlayer clientPlayer = racer.getValue();
            if (clientPlayer.getNickname() != this.cRacer.getNickname()) {
                this.updateAnOpponent(clientPlayer);
            }
        }
    }
}
