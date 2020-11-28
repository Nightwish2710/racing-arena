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

                + this.sGameMaster.getSizeInBytes(false, null);
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);

        // Pour in data
        // Always put cmd first
        byteBuffer.putInt(this.cmd);
        // Then put data sequentially
        byteBuffer.putInt(this.eventFlag);
        byteBuffer.putInt(this.sGameMaster.getCurrentNumOfRacers());

        for (Map.Entry<String, ServerRacerObject> entry : this.sGameMaster.getsRacers().entrySet()) {
            ServerRacerObject racerObject = entry.getValue();
            // string username
            byteBuffer.putInt(racerObject.getUsername().length());
            byteBuffer.put(racerObject.getUsername().getBytes());
            // int position;
            byteBuffer.putInt(racerObject.getPosition());
            // int rStatus;
            byteBuffer.putInt(racerObject.getStatus());
        }

        // Return a byte[] array
        return byteBuffer.array();
    }

    @Override
    public void unpack(byte[] bytes) {}
}
