package clientdatamodel.receive;

import clientdatamodel.ClientDataModel;

import java.nio.ByteBuffer;

public class CRecQuestion extends ClientDataModel {
    private int cQuestionID;

    private int cNum1;
    private int cOp;
    private int cNum2;

    public CRecQuestion() {
        this.cQuestionID = -1;
        this.cNum1 = -1;
        this.cOp = -1;
        this.cNum2 = -1;
    }

    @Override
    public void unpack(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.rewind();

        this.cQuestionID = byteBuffer.getInt();
        this.cNum1 = byteBuffer.getInt();
        this.cOp = byteBuffer.getInt();
        this.cNum2 = byteBuffer.getInt();
    }

    public int getcQuestionID() {
        return cQuestionID;
    }

    public int getcNum1() {
        return cNum1;
    }

    public int getcOp() {
        return cOp;
    }

    public int getcNum2() {
        return cNum2;
    }
}
