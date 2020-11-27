package clientdatamodel.receive;

import clientdatamodel.ClientDataModel;
import clientnetwork.ClientNetworkConfig;
import clientobject.ClientGameConfig;
import clientobject.Opponent;
import clientobject.Player;
import clientobject.Racer;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class CReceiveLogin extends ClientDataModel {
    private int eventFlag;
    private int clientID;
    private int racerVictory;
    private int numOfRacers;
    private HashMap<Integer, Opponent> cOpponents;

    public CReceiveLogin() {
        this.eventFlag = -1;
        this.clientID = -1;
        this.racerVictory = -1;
        cOpponents = null;
    }

    @Override
    public void unpack(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.rewind();

        this.eventFlag = byteBuffer.getInt();

        if (this.eventFlag == ClientNetworkConfig.LOGIN_FLAG.SUCCESS) {
            this.clientID = byteBuffer.getInt();
            this.racerVictory = byteBuffer.getInt();
            this.numOfRacers = byteBuffer.getInt();
            System.out.println("RECEIVE no Of racers" + this.numOfRacers);
            cOpponents = new HashMap<>();
            for (int i = 0; i < this.numOfRacers - 1; ++i) { // exclude this racer
                System.out.println("LOOP " + i);
                int rID = byteBuffer.getInt();

                int lUsername = byteBuffer.getInt();
                byte[] bUsername = new byte[lUsername];
                byteBuffer.get(bUsername);
                String rUsername = new String(bUsername);

                int rPosition = byteBuffer.getInt();

                int rStatus = byteBuffer.getInt();

                Opponent opponent = new Opponent(rID, rUsername, rPosition, 0, rStatus, ClientGameConfig.STATUS_STRING[rStatus]);
                cOpponents.put(rID, opponent);
            }
        }
    }

    public int getEventFlag() {
        return eventFlag;
    }

    public void setEventFlag(int eventFlag) {
        this.eventFlag = eventFlag;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getRacerVictory() {
        return racerVictory;
    }

    public void setRacerVictory(int racerVictory) {
        this.racerVictory = racerVictory;
    }

    public int getNumOfRacers() {
        return numOfRacers;
    }

    public void setNumOfRacers(int numOfRacers) {
        this.numOfRacers = numOfRacers;
    }

    public HashMap<Integer, Opponent> getcOpponents() {
        return cOpponents;
    }

    public void setcOpponents(HashMap<Integer, Opponent> cOpponents) {
        this.cOpponents = cOpponents;
    }
}
