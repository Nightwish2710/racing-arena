package clientdatamodel.send;

import clientdatamodel.ClientDataModel;

import java.nio.ByteBuffer;

public class CSenAnswer extends ClientDataModel {
    private int cmd;
    private int cQuestionID;
    private int cAnswer;
    private long cAnsweringTime;

    public CSenAnswer(int _cmd, int _cQuestionID, int _cAnswer, long _cAnsweringTime) {
        this.cmd = _cmd;
        this.cQuestionID = _cQuestionID;
        this.cAnswer = _cAnswer;
        this.cAnsweringTime = _cAnsweringTime;
    }

    @Override
    public byte[] pack() {
        // Allocate a bytebuffer
        int capacity = Integer.BYTES // cmd String
                + Integer.BYTES // storing size of username String
                + Integer.BYTES // storing size of password String
                + Long.BYTES; // storing size of password String
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);

        // Pour in data
        // Always put cmd first
        byteBuffer.putInt(this.cmd);
        // Then put size of each object in sequence
        byteBuffer.putInt(this.cQuestionID);
        byteBuffer.putInt(this.cAnswer);
        byteBuffer.putLong(this.cAnsweringTime);

        // Return a byte[] array
        return byteBuffer.array();
    }
}
