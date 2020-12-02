package clientobject;

public class ClientRacer extends ClientPlayer {
    private int numOfIncorrect;
    private int numOfVictory;
    private String password;

    public ClientRacer(String _nickname, int _position, int _gain, int _statusFlag, String _statusStr) {
        super(_nickname, _position, _gain, _statusFlag, _statusStr);
        this.numOfIncorrect = 0;
        this.numOfVictory = 0;
        this.password = "";
    }

    public int getNumOfIncorrect() { return this.numOfIncorrect; }
    public void setNumOfIncorrect(int numOfIncorrect) { this.numOfIncorrect = numOfIncorrect; }
    public void updateNumOfIncorrectBy(int incorrectPenalty) { this.numOfIncorrect += incorrectPenalty; }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumOfVictory() {
        return this.numOfVictory;
    }
    public void setNumOfVictory(int numOfVictory) {
        this.numOfVictory = numOfVictory;
    }
    public void updateNumOfVictoryBy(int delta) { this.numOfVictory += delta; }
}
