package clientdatamodel.receive;

import clientdatamodel.ClientDataModel;

import java.nio.ByteBuffer;

public class CRecQuestion extends ClientDataModel {
    private int cQuestionID;

    private int cNum1;
    private int cOp;
    private int cNum2;

    private long sQuestionStartTime;
    private long timeOffset;

    public CRecQuestion() {
        this.cQuestionID = -1;
        this.cNum1 = -1;
        this.cOp = -1;
        this.cNum2 = -1;
        this.timeOffset = 0;
    }

    @Override
    public void unpack(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.rewind();

        this.cQuestionID = byteBuffer.getInt();
        this.cNum1 = byteBuffer.getInt();
        this.cOp = byteBuffer.getInt();
        this.cNum2 = byteBuffer.getInt();
        this.sQuestionStartTime = byteBuffer.getLong();
        this.timeOffset = System.currentTimeMillis() - sQuestionStartTime;
    }

    public int getCQuestionID() {
        return this.cQuestionID;
    }
    public int getCNum1() {
        return this.cNum1;
    }
    public int getCOp() {
        return this.cOp;
    }
    public int getCNum2() {
        return this.cNum2;
    }
    public long getTimeOffset() { return this.timeOffset; }
}
