package clientobject;

public class Player {
    private int id;
    private String nickname;
    private int position, gain, statusFlag;
    private String statusStr;

    public Player(int _id, String _nickname, int _position, int _gain, int _statusFlag, String _statusStr) {
        this.id = _id;
        this.nickname = _nickname;
        this.position = _position;
        this.gain = _gain;
        this.statusFlag = _statusFlag;
        this.statusStr = _statusStr;
    }

    public int getId() { return this.id; }

    public int getPosition() { return this.position; }
    public void setPosition(int position) { this.position = position; }

    public String getNickname() { return this.nickname; }

    public int getGain() { return this.gain; }
    public void setGain(int gain) { this.gain = gain; }

    public void setId(int id) { this.id = id; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getStatusFlag() { return statusFlag; }
    public void setStatusFlag(int statusFlag) { this.statusFlag = statusFlag; }

    public String getStatusStr() { return statusStr; }
    public void setStatusStr(String statusStr) { this.statusStr = statusStr; }
}
