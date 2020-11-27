package clientobject;

public class Opponent {
    private int id, position;
    private String nickname, gain, status;
    
    public Opponent(int _id, int _position, String _nickname, String _gain, String _status) {
        this.id = _id;
        this.position = _position;
        this.nickname = _nickname;
        this.gain = _gain;
        this.status = _status;
    }

    public int getId() { return this.id; }

    public int getPosition() { return this.position; }
    public void setPosition(int position) { this.position = position; }

    public String getNickname() { return this.nickname; }

    public String getGain() { return this.gain; }
    public void setGain(String gain) { this.gain = gain; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
