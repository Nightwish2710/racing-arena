package clientdatamodel.receive;

import clientdatamodel.ClientDataModel;

import java.nio.ByteBuffer;

public class CRecOpponentInfo extends ClientDataModel {
    private int eventFlag;
    private String opponentUsername;
    private int opponentPosition;
    private int opponentStatus;
    private int currentNumOfRacers;

    public CRecOpponentInfo() {
        this.eventFlag = -1;
        this.opponentUsername = null;
        this.opponentPosition = -1;
        this.opponentStatus = -1;
        this.currentNumOfRacers = 0;
    }

    @Override
    public void unpack(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.rewind();

        this.eventFlag = byteBuffer.getInt();

        this.currentNumOfRacers = byteBuffer.getInt();

        int lUsername = byteBuffer.getInt();
        byte[] bUsername = new byte[lUsername];
        byteBuffer.get(bUsername);
        this.opponentUsername = new String(bUsername);

        this.opponentPosition = byteBuffer.getInt();

        this.opponentStatus = byteBuffer.getInt();
    }

    public int getEventFlag() {
        return this.eventFlag;
    }
    public void setEventFlag(int eventFlag) {
        this.eventFlag = eventFlag;
    }

    public String getOpponentUsername() {
        return this.opponentUsername;
    }
    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public int getOpponentPosition() {
        return opponentPosition;
    }
    public void setOpponentPosition(int opponentPosition) {
        this.opponentPosition = opponentPosition;
    }

    public int getOpponentStatus() {
        return this.opponentStatus;
    }
    public void setOpponentStatus(int opponentStatus) {
        this.opponentStatus = opponentStatus;
    }

    public int getCurrentNumOfRacers() {
        return this.currentNumOfRacers;
    }
    public void setCurrentNumOfRacers(int currentNumOfRacers) {
        this.currentNumOfRacers = currentNumOfRacers;
    }
}
