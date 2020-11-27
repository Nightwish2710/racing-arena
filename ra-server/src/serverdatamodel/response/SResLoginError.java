package serverdatamodel.response;

import serverdatamodel.ServerDataModel;
import java.nio.ByteBuffer;

public class SResLoginError extends ServerDataModel {
    private int cmd;
    private int eventFlag;

    public SResLoginError(int cmd, int eventFlag) {
        this.cmd = cmd;
        this.eventFlag = eventFlag;
    }

    @Override
    public byte[] pack() {
        int capacity = Integer.BYTES // cmd
                + Integer.BYTES; // storing eventFlag

        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);

        // Pour in data
        // Always put cmd first
        byteBuffer.putInt(this.cmd);
        // Then put data sequentially
        byteBuffer.putInt(this.eventFlag);

        // Return a byte[] array
        return byteBuffer.array();
    }

    @Override
    public void unpack(byte[] bytes) {}
}
