package clientobject;

public class opponent {
    private int id, position;
    private String nickname, gain, status;

    public int getId() { return this.id; }
    public void set(int id) { this.id = id; }

    public int getPosition() { return this.position; }
    public void setPosition(int position) { this.position = position; }

    public String getNickname() { return this.nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getGain() { return this.gain; }
    public void setGain(String gain) { this.gain = gain; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
