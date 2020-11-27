package serverdatamodel.response;

import serverdatamodel.ServerDataModel;
import serverobject.ServerGameMaster;
import serverobject.ServerRacerObject;

import java.nio.ByteBuffer;
import java.util.Map;

public class SResAllRacersInfo extends ServerDataModel {
    private int cmd;
    private int eventFlag;
    private ServerGameMaster sGameMaster;

    public SResAllRacersInfo(int _cmd, int _eventFlag, ServerGameMaster _sGameMaster) {
        this.cmd = _cmd;
        this.eventFlag = _eventFlag;
        this.sGameMaster = _sGameMaster;
    }

    @Override
    public byte[] pack() {
        int capacity = Integer.BYTES // cmd
                + Integer.BYTES // storing eventFlag
                + Integer.BYTES // storing numOfRacers

                + this.sGameMaster.getSizeInBytes(false, -1);
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);

        // Pour in data
        // Always put cmd first
        byteBuffer.putInt(this.cmd);
        // Then put data sequentially
        byteBuffer.putInt(this.eventFlag);
        byteBuffer.putInt(this.sGameMaster.getNumberOfRacer());

        for (Map.Entry<Integer, ServerRacerObject> entry : this.sGameMaster.getsRacers().entrySet()) {
            ServerRacerObject racerObject = entry.getValue();
            // int rID;
            byteBuffer.putInt(racerObject.getRacerID());
            // string username
            byteBuffer.put(racerObject.getUsername().getBytes());
            // string password
            byteBuffer.put(racerObject.getPassword().getBytes());
            // int rStatus;
            byteBuffer.putInt(racerObject.getStatus());
        }

        // Return a byte[] array
        return byteBuffer.array();
    }

    @Override
    public void unpack(byte[] bytes) {}
}
