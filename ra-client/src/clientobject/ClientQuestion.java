package clientobject;

public class ClientQuestion {
    private int firstNum, secondNum, operator;

    public ClientQuestion(int _firstNum, int _secondNum, int _operator) {
        this.firstNum = _firstNum;
        this.secondNum = _secondNum;
        this.operator = _operator;
    }

    public int getFirstNum() { return this.firstNum; }
    public int getSecondNum() { return this.secondNum; }
    public int getOperator() { return this.operator; }
}
