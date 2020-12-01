package servernetwork;

import serverdatabase.ServerDBConfig;
import serverdatabase.ServerDBHelper;

import serverdatamodel.ServerDataModel;
import serverdatamodel.request.SReqAccount;
import serverdatamodel.request.SReqAnswer;
import serverdatamodel.response.SResLoginError;
import serverdatamodel.response.SResLoginSuccess;
import serverdatamodel.response.SResOpponentInfo;

import serverobject.ServerGameConfig;
import serverobject.ServerGameMaster;
import serverobject.ServerQuestion;
import serverobject.ServerRacerObject;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerCSocketThread implements Runnable{
    private int cSocketID;
    private boolean isPermittedToRun;

    private Socket socketOfServer;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private ServerNetwork.ServerNetworkThread parentThread;

    private String sRacerName;

    public ServerCSocketThread(Socket _socketOfServer, int _cSocketID, ServerNetwork.ServerNetworkThread _parentThread) {
        this.cSocketID = _cSocketID;
        this.isPermittedToRun = true;

        this.socketOfServer = _socketOfServer;
        // Server socket I/O
        try {
            inStream = new DataInputStream(socketOfServer.getInputStream());
            outStream = new DataOutputStream(socketOfServer.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.parentThread = _parentThread;

        this.sRacerName = null;

        System.out.println(this.getClass().getSimpleName() + " new connection with client# " + this.cSocketID + " at " + socketOfServer);
    }

    @Override
    public void run() {
        try {
            while (this.isPermittedToRun) {
                int cmd = inStream.readInt();
                //if (cmd == ServerNetworkConfig.CMD.DISCONNECT) { break; }

                int lData = inStream.available();
                byte[] bytes = new byte[lData];
                inStream.read(bytes);

                // Switch on command id
                switch (cmd) {
                    case ServerNetworkConfig.CMD.CMD_LOGIN:
                        handleLogin(cmd, bytes, this.outStream, this.parentThread);
                        break;

                    case ServerNetworkConfig.CMD.CMD_ANSWER:
                        handleAnswer(bytes);
                        break;

                    case ServerNetworkConfig.CMD.DISCONNECT:
                        finalizeOnClose();
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void finalizeOnClose () throws IOException {
        // set isOnline status of myself to 0
        String updateUser = "UPDATE " + ServerDBConfig.TABLE_RACER
                + " SET " + ServerDBConfig.TABLE_RACER_isonline + " = 0 WHERE "
                + ServerDBConfig.TABLE_RACER_username + " = '" + this.sRacerName + "'";
        ServerDBHelper.getInstance().exec(updateUser);

        // update myself with new status: disconnected to master array
        ServerGameMaster.getInstance().getRacerByUsername(this.sRacerName).setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_QUIT);
        // signal this info to other opponents
        SResOpponentInfo sResOpponentInfo = new SResOpponentInfo(
                ServerNetworkConfig.CMD.CMD_INFO,
                ServerNetworkConfig.INFO_TYPE_FLAG.TYPE_NOTICE_UPDATE_OPPONENT,
                this.sRacerName,
                ServerGameMaster.getInstance());
        this.parentThread.signalAllClients(sResOpponentInfo, this.cSocketID, true);

        // close all streams
        inStream.close();
        outStream.close();
        // close the given socket
        socketOfServer.close();
        // remove this client socket from the array of network's client sockets
        this.parentThread.unSubscribeClientSocket(this.cSocketID);
        // break loop in run()
        this.isPermittedToRun = false;
        // tell master to remove myself
        ServerGameMaster.getInstance().removeRacer(this.sRacerName);

        System.out.println(getClass().getSimpleName() + ": Client "+ this.getsRacerName() +" disconnected");
    }

    public void reply (ServerDataModel data) {
        try {
            outStream.write(data.pack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getsRacerName() {
        return sRacerName;
    }

    private void handleLogin(int cmd, byte[] bytes, DataOutputStream outStream, ServerNetwork.ServerNetworkThread parentThread) throws SQLException, IOException {
        SReqAccount sReqAccount = new SReqAccount();
        sReqAccount.unpack(bytes);

        System.out.println(this.getClass().getSimpleName() + ": request login: " + sReqAccount.getUsername() + ", " + sReqAccount.getPassword());

        // check if there is available slots
        if (ServerGameMaster.getInstance().getCurrentNumOfRacers() < ServerGameMaster.getInstance().getNumOfRacers()) {
            // check if username exists in database
            String queryUser = "SELECT * FROM " + ServerDBConfig.TABLE_RACER
                    + " WHERE " + ServerDBConfig.TABLE_RACER_username + " = '" + sReqAccount.getUsername() + "'";
            ResultSet user = ServerDBHelper.getInstance().execForResult(queryUser);

            if (user != null && !user.isClosed()) {
                if (user.next()) {
                    user.beforeFirst();
                    // if it is, check if password match, expected one result
                    while (!user.isClosed() && user.next()) {
                        String uPassword = user.getString(ServerDBConfig.TABLE_RACER_password);

                        if (uPassword.equals(sReqAccount.getPassword())) {
                            // if password match, check if duplicated login by isOnline
                            int isOnline = user.getInt(ServerDBConfig.TABLE_RACER_isonline);

                            if (isOnline == 1) {
                                System.out.println(this.getClass().getSimpleName() + ": duplicated login");

                                SResLoginError sResLoginError = new SResLoginError(cmd, ServerNetworkConfig.LOGIN_FLAG.DUPLICATED_LOGIN);
                                outStream.write(sResLoginError.pack());
                            }
                            else {
                                // create existing racer and add to master, set isonline, set this racer to this thread's owner,
                                // send individually (success login) and bulk (update number of racers to all)
                                System.out.println(this.getClass().getSimpleName() + ": exist user");

                                int victory = user.getInt(ServerDBConfig.TABLE_RACER_victory);
                                ServerRacerObject sRacer = new ServerRacerObject(sReqAccount.getUsername(), sReqAccount.getPassword(), victory);
                                ServerGameMaster.getInstance().addSRacer(sRacer);

                                String updateUser = "UPDATE " + ServerDBConfig.TABLE_RACER
                                        + " SET " + ServerDBConfig.TABLE_RACER_isonline + " = 1 WHERE "
                                        + ServerDBConfig.TABLE_RACER_username + " = '" + sReqAccount.getUsername() + "'";
                                ServerDBHelper.getInstance().exec(updateUser);

                                this.sRacerName = sReqAccount.getUsername();

                                SResLoginSuccess sResLoginSuccess = new SResLoginSuccess(cmd, ServerNetworkConfig.LOGIN_FLAG.SUCCESS, sReqAccount.getUsername(), victory, ServerGameMaster.getInstance());
                                outStream.write(sResLoginSuccess.pack());

                                SResOpponentInfo sResOpponentInfo = new SResOpponentInfo(ServerNetworkConfig.CMD.CMD_INFO, ServerNetworkConfig.INFO_TYPE_FLAG.TYPE_NOTICE_NEW_OPPONENT, sReqAccount.getUsername(), ServerGameMaster.getInstance());
                                this.parentThread.signalAllClients(sResOpponentInfo, this.cSocketID, true);
                            }
                        }
                        else {
                            // if password not match, username duplicate error, not record login, send individually (username has been taken)
                            System.out.println(this.getClass().getSimpleName() + ": name taken");

                            SResLoginError sResLoginError = new SResLoginError(cmd, ServerNetworkConfig.LOGIN_FLAG.USERNAME_TAKEN);
                            outStream.write(sResLoginError.pack());
                        }
                    }
                }
                else {
                    // if it is not, create new racer and add to master, record database, update numberOfJoiningRacers to server, set this racer to this thread's owner,
                    // send individually (success login) and bulk (update number of racers to all)
                    System.out.println(this.getClass().getSimpleName() + ": new user");

                    int victory = 0;
                    ServerRacerObject sRacer = new ServerRacerObject(sReqAccount.getUsername(), sReqAccount.getPassword(), victory);
                    ServerGameMaster.getInstance().addSRacer(sRacer);

                    String insertUser = "INSERT INTO " + ServerDBConfig.TABLE_RACER + " VALUES ("
                            + "'" + sReqAccount.getUsername() + "', "
                            + "'" + sReqAccount.getPassword() + "', "
                            + victory + ", "
                            + "1) ";
                    ServerDBHelper.getInstance().exec(insertUser);

                    this.sRacerName = sReqAccount.getUsername();

                    SResLoginSuccess sResLoginSuccess = new SResLoginSuccess(cmd, ServerNetworkConfig.LOGIN_FLAG.SUCCESS, sReqAccount.getUsername(), victory, ServerGameMaster.getInstance());
                    outStream.write(sResLoginSuccess.pack());

                    SResOpponentInfo sResOpponentInfo = new SResOpponentInfo(ServerNetworkConfig.CMD.CMD_INFO, ServerNetworkConfig.INFO_TYPE_FLAG.TYPE_NOTICE_NEW_OPPONENT, sReqAccount.getUsername(), ServerGameMaster.getInstance());
                    this.parentThread.signalAllClients(sResOpponentInfo, this.cSocketID, true);
                }
            }
        }
        else {
            // if no, not record login, send individually (no more slots)
            SResLoginError sResLoginError = new SResLoginError(cmd, ServerNetworkConfig.LOGIN_FLAG.NO_MORE_SLOTS);
            outStream.write(sResLoginError.pack());
        }
    }

    private void handleAnswer(byte[] bytes) {
        SReqAnswer sReqAnswer = new SReqAnswer();
        sReqAnswer.unpack(bytes);

        ServerRacerObject thisRacer = ServerGameMaster.getInstance().getRacerByUsername(this.sRacerName);

        ServerQuestion currentSQuestion = ServerGameMaster.getInstance().getQuestion(sReqAnswer.getCQuestionID());

        // check for time-out first
        long sDeltaAnsweringTime = sReqAnswer.getCAnsweringTime() - currentSQuestion.getStartingTimeOfQuestion();
        thisRacer.setCurrDeltaSAnsweringTime(sDeltaAnsweringTime);
        System.out.println(getClass().getSimpleName() + "this racer init pos: " + thisRacer.getPosition());
        System.out.println(getClass().getSimpleName() + ": " + sReqAnswer.getCAnswer() + " " + sReqAnswer.getCQuestionID() + " " + sDeltaAnsweringTime);

        if (sDeltaAnsweringTime <= ServerGameConfig.MAX_TIMER_MILIS) {
            System.out.println(thisRacer.getUsername() + ": IN TIME");
            // not timeout, go check for correctness
            // get actual answer from server
            int sAnswer = currentSQuestion.getAnswer();
            if (sAnswer == sReqAnswer.getCAnswer()) {
                System.out.println(thisRacer.getUsername() + ": NORMAL");
                // correct answer, get 1 point, status normal
                thisRacer.updatePositionBy(ServerGameConfig.GAME_BALANCE.GAIN_NORMAL);
                thisRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_NORMAL);
            } else {
                System.out.println(thisRacer.getUsername() + ": WRONG");
                // incorrect answer, get -1 point, status wrong
                thisRacer.updatePositionBy(ServerGameConfig.GAME_BALANCE.GAIN_WRONG);
                thisRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_WRONG);
                thisRacer.updateNumOfWrongBy(1);
            }
        } else {
            System.out.println(thisRacer.getUsername() + ": TIME OUT");
            // timeout
            thisRacer.updatePositionBy(ServerGameConfig.GAME_BALANCE.GAIN_TIMEOUT);
            thisRacer.setStatus(ServerGameConfig.RACER_STATUS_FLAG.FLAG_TIMEOUT);
        }
    }
}
