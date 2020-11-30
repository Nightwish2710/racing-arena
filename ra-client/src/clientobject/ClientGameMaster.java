package clientobject;

import clientGUI.ClientGUI;

import java.util.HashMap;
import java.util.Map;

public class ClientGameMaster {
    private int numOfRacers;
    private ClientRacer cRacer;
    private HashMap<String, ClientOpponent> cOpponents; // <username, opponentObject>
    private ClientQuestion currentQuestion;

    // Singleton
    private static ClientGameMaster clientGameMaster = null;
    public static ClientGameMaster getInstance() {
        if (clientGameMaster == null) {
            clientGameMaster = new ClientGameMaster();
        }
        return  clientGameMaster;
    }

    public ClientGameMaster() {
        this.cRacer = new ClientRacer("", 0, 0, -1, "");
        this.numOfRacers = 0;
        this.cOpponents = null;
        this.currentQuestion = null;
        clientGameMaster = this;
    }

    public ClientRacer getcRacer() { return this.cRacer; }
    public void setcRacer(ClientRacer cClientRacer) { this.cRacer = cClientRacer; }

    public HashMap<String, ClientOpponent> getcOpponents() { return this.cOpponents; }

    public int getNumOfRacers() { return this.numOfRacers; }
    public void setNumOfRacers(int numOfRacers) { this.numOfRacers = numOfRacers; }

    public int getCurentNumOfRacers() { return this.cOpponents.size() + 1; }

    public void setInitCOpponents(HashMap<String, ClientOpponent> cOpponents) {
        this.cOpponents = cOpponents;

        ClientGUI.getInstance().initOpponentBarWhenReceiveNumOfPplJoinning();

        int order = 2;
        for (Map.Entry<String, ClientOpponent> opps : this.cOpponents.entrySet()) {
            System.out.println("PREV OPPOs: " + opps.getKey());

            ClientGUI.getInstance().updateOpponentProgress(order, opps.getValue());
            order += 1;
        }
    }

    public void addNewOpponent(ClientOpponent cNewOpponent) {
        System.out.println("NEW OPPOs: " + cNewOpponent.getNickname());
        this.cOpponents.put(cNewOpponent.getNickname(), cNewOpponent);
        int order = this.getCurentNumOfRacers();
        ClientGUI.getInstance().updateOpponentProgress(order, cNewOpponent);
    }

    public void updateAnOpponent(ClientOpponent cOpponent) {
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
}
