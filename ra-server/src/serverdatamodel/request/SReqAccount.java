package serverdatamodel.request;

import serverdatamodel.ServerDataModel;

import java.nio.ByteBuffer;

public class SReqAccount extends ServerDataModel {
    private String username;
    private String password;

    public SReqAccount() {
        this.username = null;
        this.password = null;
    }

    @Override
    public void unpack(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.rewind();

        // actual data
        int lUsername = byteBuffer.getInt();
        int lPassword = byteBuffer.getInt();

        byte[] bUsername = new byte[lUsername];
        byte[] bPassword = new byte[lPassword];

        byteBuffer.get(bUsername);
        byteBuffer.get(bPassword);

        this.username = new String(bUsername);
        this.password = new String(bPassword);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
