package clientobject;

import java.util.HashMap;

public class ClientGameMaster {
    private ClientRacer cRacer;
    private int numOfRacers;
    private HashMap<String, ClientOpponent> cOpponents;

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

    public ClientRacer getcRacer() {
        return cRacer;
    }
    public void setcRacer(ClientRacer cClientRacer) {
        this.cRacer = cClientRacer;
    }

    public HashMap<String, ClientOpponent> getcOpponents() {
        return cOpponents;
    }

    public void setcOpponents(HashMap<String, ClientOpponent> cOpponents) {
        this.cOpponents = cOpponents;
    }

    public int getNumOfRacers() {
        return numOfRacers;
    }

    public void setNumOfRacers(int numOfRacers) {
        this.numOfRacers = numOfRacers;
    }
}
