package clientdatamodel;

import java.nio.ByteBuffer;

public class ClientDataModel {
    private ByteBuffer byteBuffer;

    public ClientDataModel() { this.byteBuffer = null; }

    public byte[] pack() { return this.byteBuffer.array(); };
    public void unpack(byte[] bytes) {};
}
