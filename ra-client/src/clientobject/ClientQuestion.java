package clientobject;

public class ClientQuestion {
    private int questionId, firstNum, operator, secondNum;
    private long timeOffset;

    public ClientQuestion(int _questionId, int _firstNum, int _operator, int _secondNum, long _timeOffset) {
        this.questionId = _questionId;
        this.firstNum = _firstNum;
        this.operator = _operator;
        this.secondNum = _secondNum;
        this.timeOffset = _timeOffset;
    }

    public int getQuestionId() { return this.questionId; }
    public int getFirstNum() { return this.firstNum; }
    public int getOperator() { return this.operator; }
    public int getSecondNum() { return this.secondNum; }
    public long getTimeOffset() { return this.timeOffset; }
}
