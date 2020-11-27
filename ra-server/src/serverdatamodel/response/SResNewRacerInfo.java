package serverdatamodel.response;

import serverdatamodel.ServerDataModel;
import serverobject.ServerGameMaster;
import serverobject.ServerRacerObject;

import java.nio.ByteBuffer;
import java.util.Map;

public class SResNewRacerInfo extends ServerDataModel {
    private int cmd;
    private int eventFlag;
    private int newRacerID;
    private ServerGameMaster sGameMaster;

    public SResNewRacerInfo(int _cmd, int _eventFlag, int _newRacerID, ServerGameMaster _sGameMaster) {
        this.cmd = _cmd;
        this.eventFlag = _eventFlag;
        this.newRacerID = _newRacerID;
        this.sGameMaster = _sGameMaster;
    }

    @Override
    public byte[] pack() {
        int capacity = Integer.BYTES // cmd
                + Integer.BYTES // storing eventFlag
                + Integer.BYTES // storing numOfRacers

                + this.sGameMaster.getSizeInBytesOfRacer(this.newRacerID);
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);

        // Pour in data
        // Always put cmd first
        byteBuffer.putInt(this.cmd);
        // Then put data sequentially
        byteBuffer.putInt(this.eventFlag);
        byteBuffer.putInt(this.sGameMaster.getCurrentNumOfRacers());

        ServerRacerObject newRacer = this.sGameMaster.getRacerInfo(this.newRacerID);
        // int rID;
        byteBuffer.putInt(newRacer.getRacerID());
        // string username
        byteBuffer.putInt(newRacer.getUsername().length());
        byteBuffer.put(newRacer.getUsername().getBytes());
        // int position;
        byteBuffer.putInt(newRacer.getPosition());
        // int rStatus;
        byteBuffer.putInt(newRacer.getStatus());

        // Return a byte[] array
        return byteBuffer.array();
    }

    @Override
    public void unpack(byte[] bytes) {}
}
