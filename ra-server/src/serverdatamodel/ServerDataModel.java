package serverdatamodel;

import java.nio.ByteBuffer;

public class ServerDataModel {
    private ByteBuffer byteBuffer;

    public ServerDataModel() {
        this.byteBuffer = null;
    }

    public byte[] pack() {
        return this.byteBuffer.array();
    };

    public void unpack(byte[] bytes) {};
}
