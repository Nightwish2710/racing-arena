package clientobject;

import clientGUI.ClientGUI;

import java.util.HashMap;
import java.util.Map;

public class ClientGameMaster {
    private int numOfRacers;
    private int curentNumOfRacers;
    private ClientRacer cRacer;
    private HashMap<String, ClientOpponent> cOpponents;

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
        clientGameMaster = this;
    }

    public ClientRacer getcRacer() { return this.cRacer; }
    public void setcRacer(ClientRacer cClientRacer) { this.cRacer = cClientRacer; }

    public HashMap<String, ClientOpponent> getcOpponents() { return this.cOpponents; }

    public int getNumOfRacers() { return this.numOfRacers; }
    public void setNumOfRacers(int numOfRacers) { this.numOfRacers = numOfRacers; }

    public int getCurentNumOfRacers() { return this.curentNumOfRacers; }
    public void setCurentNumOfRacers(int curentNumOfRacers) { this.curentNumOfRacers = curentNumOfRacers; }

    public void setInitCOpponents(HashMap<String, ClientOpponent> cOpponents) {
        this.cOpponents = cOpponents;

        ClientGUI.getInstance().initOpponentBarWhenReceiveNumOfPplJoinning();

        int order = 2;
        for (Map.Entry<String, ClientOpponent> opps : this.cOpponents.entrySet()) {
            System.out.println(getClass().getSimpleName() + " got: " + opps.getKey() + " - " + opps.getValue().getStatusFlag());

            ClientGUI.getInstance().updateOpponentProgress(order, opps.getValue());
            order += 1;
        }
    }

    public void addNewOpponent(ClientOpponent cNewOpponent) {
        this.cOpponents.put(cNewOpponent.getNickname(), cNewOpponent);
        int order = this.getCurentNumOfRacers();
        ClientGUI.getInstance().updateOpponentProgress(order, cNewOpponent);
    }
}
