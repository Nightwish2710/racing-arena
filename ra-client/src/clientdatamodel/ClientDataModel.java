package clientdatamodel;

import clientnetwork.ClientNetworkConfig;

import java.nio.ByteBuffer;

public class ClientDataModel {
    private ByteBuffer byteBuffer;

    public ClientDataModel() {
        this.byteBuffer = null;
    }

    public byte[] pack(int cmd) {
        return byteBuffer.array();
    };

    public void unpack(ByteBuffer byteBuffer) {
        return;
    };
}
