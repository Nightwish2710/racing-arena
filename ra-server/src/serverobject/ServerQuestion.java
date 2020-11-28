package serverobject;

import java.util.concurrent.ThreadLocalRandom;

public class ServerQuestion {
    private static final int MAX = 10000, MIN = -10000;
    private static final String[] OPERATORS = {"+", "-", "*", "/", "%"};

    private int firstNum, secondNum;
    private String operator;

    public ServerQuestion() {
        firstNum = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
        secondNum = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);

        int index = ThreadLocalRandom.current().nextInt(0, 4 + 1);
        operator = OPERATORS[index];
    }

    public int getFirstNum() { return this.firstNum; }
    public int getSecondNum() { return this.secondNum; }
    public String getOperator() { return this.operator; }
}
