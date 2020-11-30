package serverobject;

public class ServerRacerObject {
    private String username;
    private String password; // private to individual
    private int numOfVictory; // private to individual
    private int position;
    private int status;

    private int gain;
    private int numOfWrong;
    private long currDeltaSAnsweringTime;

    public ServerRacerObject(String _username, String _password, int _numOfVictory) {
        this.username = _username;
        this.password = _password;
        this.numOfVictory = _numOfVictory;
        this.position = ServerGameConfig.INIT_RACER_POSITION;
        this.status = ServerGameConfig.RACER_STATUS_FLAG.FLAG_READY;

        this.gain = ServerGameConfig.INIT_RACER_GAIN;
        this.numOfWrong = 0;
        this.currDeltaSAnsweringTime = 0;
    }

    public ServerRacerObject() {
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getNumOfVictory() { return numOfVictory; }
    public void setNumOfVictory(int numOfVictory) { this.numOfVictory = numOfVictory; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public void updatePositionBy(int delta) {
        this.gain = delta;
        this.position += delta;
        if (this.position < ServerGameConfig.INIT_RACER_POSITION) {
            this.position = ServerGameConfig.INIT_RACER_POSITION;
        }
    }

    public int getGain() { return gain; }
    public void setGain(int gain) { this.gain = gain; }

    public int getNumOfWrong() {
        return numOfWrong;
    }

    public void setNumOfWrong(int numOfWrong) {
        this.numOfWrong = numOfWrong;
    }

    public void updateNumOfWrongBy(int delta) {
        this.numOfWrong += delta;
    }

    public long getCurrDeltaSAnsweringTime() {
        return currDeltaSAnsweringTime;
    }

    public void setCurrDeltaSAnsweringTime(long currDeltaSAnsweringTime) {
        this.currDeltaSAnsweringTime = currDeltaSAnsweringTime;
    }
}
