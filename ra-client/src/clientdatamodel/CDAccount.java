package clientdatamodel;

import clientnetwork.ClientNetworkConfig;

import java.nio.ByteBuffer;

public class CDAccount extends ClientDataModel{
    private String username;
    private String password;

    public CDAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public byte[] pack(int cmd) {
        // Allocate a bytebuffer
        int capacity = Integer.BYTES // cmd String
                + Integer.BYTES // storing size of username String
                + Integer.BYTES // storing size of password String
                + username.length() // actual username String
                + password.length(); // actual password String
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);

        // Pour in data
        // Always put cmd first
        byteBuffer.putInt(cmd);
        // Then put size of each object in sequence
        byteBuffer.putInt(username.length());
        byteBuffer.putInt(password.length());
        // Put the objects following the sequence
        byteBuffer.put(username.getBytes());
        byteBuffer.put(password.getBytes());

        // Return a byte[] array
        return byteBuffer.array();
    }

    @Override
    public void unpack(ByteBuffer byteBuffer) {
        return;
    }
}
