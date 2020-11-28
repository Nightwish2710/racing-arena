package serverobject;

public class ServerRacerObject {
    private String username;
    private String password; // private to individual
    private int numOfVictory; // private to individual
    private int position;
    private int status;

    public ServerRacerObject(String _username, String _password, int _numOfVictory) {
        this.username = _username;
        this.password = _password;
        this.numOfVictory = _numOfVictory;
        this.position = ServerGameConfig.INIT_RACER_POSITION;
        this.status = ServerGameConfig.FLAG_READY;
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
}
