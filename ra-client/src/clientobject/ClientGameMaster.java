package clientobject;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientGameMaster {
    private Racer cRacer;
    private int numOfRacers;
    private HashMap<Integer, Opponent> cOpponents;

    private static ClientGameMaster clientGameMaster = null;
    public static ClientGameMaster getInstance() {
        if (clientGameMaster == null) {
            clientGameMaster = new ClientGameMaster();
        }
        return  clientGameMaster;
    }

    public ClientGameMaster() {
        this.cRacer = new Racer(0, "", 0, 0, -1, "");
        this.numOfRacers = 0;
        this.cOpponents = null;
        clientGameMaster = this;
    }

    public Racer getcRacer() {
        return cRacer;
    }
    public void setcRacer(Racer cRacer) {
        this.cRacer = cRacer;
    }

    public HashMap<Integer, Opponent> getcOpponents() {
        return cOpponents;
    }

    public void setcOpponents(HashMap<Integer, Opponent> cOpponents) {
        this.cOpponents = cOpponents;
    }

    public int getNumOfRacers() {
        return numOfRacers;
    }

    public void setNumOfRacers(int numOfRacers) {
        this.numOfRacers = numOfRacers;
    }
}
