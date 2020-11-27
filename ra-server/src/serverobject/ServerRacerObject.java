package serverobject;

public class ServerRacerObject {
    private String username;
    private String password;
    private int racerID;
    private int numOfVictory;
    private int status;

    public ServerRacerObject(String _username, String _password, int _racerID, int _numOfVictory) {
        this.username = _username;
        this.password = _password;
        this.racerID = _racerID;
        this.numOfVictory = _numOfVictory;
    }

    public ServerRacerObject() {
    }

    public int getRacerID() {
        return racerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRacerID(int racerID) {
        this.racerID = racerID;
    }

    public int getNumOfVictory() {
        return numOfVictory;
    }

    public void setNumOfVictory(int numOfVictory) {
        this.numOfVictory = numOfVictory;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
