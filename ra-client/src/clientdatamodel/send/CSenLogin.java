package clientdatamodel.send;

import clientdatamodel.ClientDataModel;
import java.nio.ByteBuffer;

public class CSenLogin extends ClientDataModel {
    private int cmd;
    private String username;
    private String password;

    public CSenLogin(int _cmd, String _username, String _password) {
        this.cmd = _cmd;
        this.username = _username;
        this.password = _password;
    }
    
    @Override
    public byte[] pack() {
        // Allocate a bytebuffer
        int capacity = Integer.BYTES // cmd String
                + Integer.BYTES // storing size of username String
                + Integer.BYTES // storing size of password String
                + this.username.length() // actual username String
                + this.password.length(); // actual password String
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);

        // Pour in data
        // Always put cmd first
        byteBuffer.putInt(this.cmd);
        // Then put size of each object in sequence
        byteBuffer.putInt(this.username.length());
        byteBuffer.putInt(this.password.length());
        // Put the objects following the sequence
        byteBuffer.put(this.username.getBytes());
        byteBuffer.put(this.password.getBytes());

        // Return a byte[] array
        return byteBuffer.array();
    }
}
