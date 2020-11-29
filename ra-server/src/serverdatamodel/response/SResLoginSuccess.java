package serverdatamodel.response;

import serverdatamodel.ServerDataModel;
import serverobject.ServerGameMaster;
import serverobject.ServerRacerObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

public class SResLoginSuccess extends ServerDataModel {
    private int cmd;
    private int eventFlag;
    private String cUsername;
    private int racerVictory;
    private ServerGameMaster sGameMaster;

    public SResLoginSuccess(int _cmd, int _eventFlag, String _cUsername, int _racerVictory, ServerGameMaster _sGameMaster) {
        this.cmd = _cmd;
        this.eventFlag = _eventFlag;
        this.cUsername = _cUsername;
        this.racerVictory = _racerVictory;
        this.sGameMaster = _sGameMaster;
    }

    @Override
    public byte[] pack() {
        int capacity = Integer.BYTES // cmd
                + Integer.BYTES // storing eventFlag
                + Integer.BYTES // storing racerVictory
                + Integer.BYTES // storing numOfRacers
                + Integer.BYTES // storing racerLength
                + Integer.BYTES // storing currentNumOfRacers
                + this.sGameMaster.getSizeInBytes(true, this.cUsername); // storing racers array

        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);

        // Pour in data
        // Always put cmd first
        byteBuffer.putInt(this.cmd);
        // Then put data sequentially
        byteBuffer.putInt(this.eventFlag);
        byteBuffer.putInt(this.racerVictory);
        byteBuffer.putInt(this.sGameMaster.getNumOfRacers());
        byteBuffer.putInt(this.sGameMaster.getRaceLength());
        byteBuffer.putInt(this.sGameMaster.getCurrentNumOfRacers());

        for (Map.Entry<String, ServerRacerObject> entry : this.sGameMaster.getsRacers().entrySet()) {
            if (!entry.getKey().equals(this.cUsername)) {
                ServerRacerObject racerObject = entry.getValue();

                // string username
                byteBuffer.putInt(racerObject.getUsername().length());
                byteBuffer.put(racerObject.getUsername().getBytes());

                // int position;
                byteBuffer.putInt(racerObject.getPosition());

                // int rStatus;
                byteBuffer.putInt(racerObject.getStatus());
            }
        }

        // Return a byte[] array
        return byteBuffer.array();
    }

    @Override
    public void unpack(byte[] bytes) {}
}
