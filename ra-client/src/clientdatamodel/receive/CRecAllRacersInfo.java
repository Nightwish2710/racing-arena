package clientdatamodel.receive;

import clientdatamodel.ClientDataModel;
import clientobject.ClientGameConfig;
import clientobject.ClientPlayer;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class CRecAllRacersInfo extends ClientDataModel {
    private int currentNumOfRacers;
    private int correctAnswer;
    private HashMap<String, ClientPlayer> cPlayers;

    public CRecAllRacersInfo() {
        this.currentNumOfRacers = -1;
        this.correctAnswer = Integer.MAX_VALUE;
        this.cPlayers = null;
    }

    @Override
    public void unpack(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.rewind();

        this.correctAnswer = byteBuffer.getInt();
        this.currentNumOfRacers = byteBuffer.getInt();

        cPlayers = new HashMap<>();
        for (int i = 0; i < this.currentNumOfRacers; ++i) {
            int lUsername = byteBuffer.getInt();
            byte[] bUsername = new byte[lUsername];
            byteBuffer.get(bUsername);

            String rUsername = new String(bUsername);

            int rPosition = byteBuffer.getInt();

            int rStatus = byteBuffer.getInt();

            ClientPlayer clientOpponent = new ClientPlayer(rUsername, rPosition, 0, rStatus, ClientGameConfig.STATUS_STRING[rStatus]);
            cPlayers.put(rUsername, clientOpponent);
        }
    }

    public int getCorrectAnswer() { return this.correctAnswer; }

    public int getCurrentNumOfRacers() {
        return this.currentNumOfRacers;
    }

    public HashMap<String, ClientPlayer> getAllRacers() {
        return this.cPlayers;
    }

    public ClientPlayer getThisRacer(String cRacerUsername) {
        return this.cPlayers.get(cRacerUsername);
    }
}
