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
        this.cRacer = new ClientRacer(
                "",
                ClientGameConfig.INIT_RACER_POSITION,
                0,
                ClientGameConfig.RACER_STATUS_FLAG.FLAG_READY,
                ClientGameConfig.STATUS_STRING[ClientGameConfig.RACER_STATUS_FLAG.FLAG_READY]);

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

        ClientGUI.getInstance().initOpponentProgressWhenReceiveNumOfPplJoinning();

        for (Map.Entry<String, ClientPlayer> opps : this.cOpponents.entrySet()) {
            System.out.println("PREV OPPOs: " + opps.getKey());
            ClientGUI.getInstance().updateOpponentNameWhenJoin(opps.getValue());
        }
    }

    public void addNewOpponent(ClientPlayer cNewOpponent) {
        System.out.println("NEW OPPOs: " + cNewOpponent.getNickname());
        this.cOpponents.put(cNewOpponent.getNickname(), cNewOpponent);
        ClientGUI.getInstance().updateOpponentNameWhenJoin(cNewOpponent);
        ClientGUI.getInstance().updateOpponentNameWhenJoin(cNewOpponent);
    }

    public void updateAnOpponent(ClientPlayer cOpponent) {
        // replace old info
        this.cOpponents.put(cOpponent.getNickname(), cOpponent);

        switch (cOpponent.getStatusFlag()) {
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_READY:
                ClientGUI.getInstance().resetProgressBarForReplay(cOpponent);
                break;
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_NORMAL:
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_FASTEST:
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_WRONG:
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_TIMEOUT:
                ClientGUI.getInstance().updateOpponentProgress(cOpponent);
                break;
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED:
                ClientGUI.getInstance().strikeThroughEliminatedRacer(cOpponent);
                break;
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_QUIT:
                ClientGUI.getInstance().updateOpponentProgressWhenARacerQuit(cOpponent);
                this.cOpponents.remove(cOpponent.getNickname());
                break;
            default:
                break;
        }
    }

    public void confirmRacerPostLogin(int numOfVictory) {
        this.cRacer.setNumOfVictory(numOfVictory);

        ClientGUI.getInstance().updateYouNickname(this.cRacer.getNickname()); // update racer name in status panel on UI
        ClientGUI.getInstance().updateYouNumOfVictory(numOfVictory); // update reacer's victory count on UI
    }

    public void setCurrentQuestion(ClientQuestion currentQuestion) {
        this.currentQuestion = currentQuestion;

//        if (this.currentQuestion.getQuestionId() == ClientGameConfig.INIT_QUESTION_ID) {
//            // put all racers back to the start
//            prepareRacersToStartTheRace();
//        }

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

    private void prepareRacersToStartTheRace() {
        _prepareRacer();
        _prepareOpponents();
    }

    private void _prepareRacer() {
        cRacer.setPosition(ClientGameConfig.INIT_RACER_POSITION);
        cRacer.setStatusFlag(ClientGameConfig.RACER_STATUS_FLAG.FLAG_READY);
        cRacer.setStatusStr(ClientGameConfig.STATUS_STRING[ClientGameConfig.RACER_STATUS_FLAG.FLAG_READY]);
        cRacer.setNumOfIncorrect(0);
        cRacer.setGain(0);

        // update UI
        ClientGUI.getInstance().resetYouProgressBar();
    }

    private void _prepareOpponents() {
        for (Map.Entry<String, ClientPlayer> opps : cOpponents.entrySet()) {
            ClientGUI.getInstance().updateOpponentNameWhenJoin(opps.getValue());
            //
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

    public void updateThisRacer() {
        // new position, new status on UI
        ClientGUI.getInstance().updateYouPoint(cRacer.getPosition());
        ClientGUI.getInstance().setUpdateStatus(ClientGameConfig.STATUS_STRING[cRacer.getStatusFlag()]);

        String gainStr = cRacer.getGain() >= 0 ? ("+"+String.valueOf(cRacer.getGain())) : String.valueOf(cRacer.getGain());
        ClientGUI.getInstance().setUpdateExtraStatus("Gain: " + gainStr + " ");
        System.out.println(getClass().getSimpleName() + ": racer status flag: " + cRacer.getStatusFlag());

        switch (cRacer.getStatusFlag()) {
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_READY:
                ClientGUI.getInstance().resetYouProgressBar();
                break;
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_WRONG:
                cRacer.updateNumOfIncorrectBy(1);
                ClientGUI.getInstance().setUpdateExtraStatus("Wrong: " + this.cRacer.getNumOfIncorrect() + " times ");
                break;
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_TIMEOUT:
                break;
            case ClientGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED:
                ClientGUI.getInstance().strikeThroughYouNickname();
                ClientGUI.getInstance().setUpdateExtraStatus("You were ejected :>> ");
                break;
            default:
                break;
        }
    }

    public void updateAllOpponents(HashMap<String, ClientPlayer> allRacers) {
        for (Map.Entry<String, ClientPlayer> racer : allRacers.entrySet()) {
            ClientPlayer clientPlayer = racer.getValue();
            if (clientPlayer.getNickname() != cRacer.getNickname()) {
                updateAnOpponent(clientPlayer);
            }
        }
    }
}
