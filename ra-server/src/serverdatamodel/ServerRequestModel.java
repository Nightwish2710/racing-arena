package serverdatamodel;

import java.nio.ByteBuffer;

public class ServerRequestModel {
    private ByteBuffer byteBuffer;

    public ServerRequestModel() {
        this.byteBuffer = null;
    }

    public byte[] pack() {
        return byteBuffer.array();
    };
    public void unpack(byte[] bytes) {
        System.out.println("HERE");
    };
}
