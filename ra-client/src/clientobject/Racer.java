package clientobject;

public class Racer {
    private int id, position, numOfIncorrect;
    private String nickname, gain, status;

    public Racer(int _id, int _position, String _nickname, String _gain, String _status) {
        this.id = _id;
        this.position = _position;
        this.nickname = _nickname;
        this.gain = _gain;
        this.status = _status;
        this.numOfIncorrect = 0;
    }

    public int getId() { return this.id; }

    public int getPosition() { return this.position; }
    public void setPosition(int position) { this.position = position; }

    public String getNickname() { return this.nickname; }

    public String getGain() { return this.gain; }
    public void setGain(String gain) { this.gain = gain; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getNumOfIncorrect() { return this.numOfIncorrect; }
    public void setNumOfIncorrect(int numOfIncorrect) { this.numOfIncorrect = numOfIncorrect; }
}
