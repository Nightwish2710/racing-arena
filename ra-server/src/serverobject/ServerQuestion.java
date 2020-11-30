package serverobject;

import java.util.concurrent.ThreadLocalRandom;

public class ServerQuestion {
    private int firstNum, secondNum, operator;

    public ServerQuestion() {
        firstNum = ThreadLocalRandom.current().nextInt(ServerGameConfig.MIN_NUMBER, ServerGameConfig.MAX_NUMBER + 1);
        secondNum = ThreadLocalRandom.current().nextInt(ServerGameConfig.MIN_NUMBER, ServerGameConfig.MAX_NUMBER + 1);
        operator = ThreadLocalRandom.current().nextInt(0, ServerGameConfig.OPERATORS.length);

        while (secondNum == 0 &&
                (operator == ServerGameConfig.OPERATOR_FLAG.DIVIDE_OP || operator == ServerGameConfig.OPERATOR_FLAG.MODULA_OP)) {
            secondNum = ThreadLocalRandom.current().nextInt(ServerGameConfig.MIN_NUMBER, ServerGameConfig.MAX_NUMBER + 1);
            operator = ThreadLocalRandom.current().nextInt(0, ServerGameConfig.OPERATORS.length);
        }
    }

    public int getFirstNum() { return this.firstNum; }
    public int getSecondNum() { return this.secondNum; }
    public int getOperator() { return this.operator; }

    public int getAnswer() {
        switch (operator) {
            case ServerGameConfig.OPERATOR_FLAG.ADD_OP:
                return firstNum + secondNum;
            case ServerGameConfig.OPERATOR_FLAG.MINUS_OP:
                return firstNum - secondNum;
            case ServerGameConfig.OPERATOR_FLAG.MULTIPLY_OP:
                return firstNum * secondNum;
            case ServerGameConfig.OPERATOR_FLAG.DIVIDE_OP:
                return firstNum / secondNum;
            case ServerGameConfig.OPERATOR_FLAG.MODULA_OP:
                return firstNum % secondNum;
            default:
                return 100000001;
        }
    }
}
