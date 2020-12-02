package serverdatamodel.request;

import serverdatamodel.ServerDataModel;

import java.nio.ByteBuffer;

public class SReqAnswer extends ServerDataModel {
    private int cQuestionID;
    private int cAnswer;
    private long cAnsweringTime;

    public SReqAnswer() {
        this.cQuestionID = -1;
        this.cAnswer = -1;
        this.cAnsweringTime = 0;
    }

    @Override
    public void unpack(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.rewind();

        this.cQuestionID = byteBuffer.getInt();
        this.cAnswer = byteBuffer.getInt();
        this.cAnsweringTime = byteBuffer.getLong();
    }

    public int getCQuestionID() {
        return this.cQuestionID;
    }

    public int getCAnswer() {
        return this.cAnswer;
    }

    public long getCAnsweringTime() {
        return this.cAnsweringTime;
    }
}
