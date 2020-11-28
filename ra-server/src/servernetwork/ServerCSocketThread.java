package servernetwork;

import serverdatabase.ServerDBConfig;
import serverdatabase.ServerDBHelper;
import serverdatamodel.ServerDataModel;
import serverdatamodel.request.SReqAccount;
import serverdatamodel.response.SResAllRacersInfo;
import serverdatamodel.response.SResLoginError;
import serverdatamodel.response.SResLoginSuccess;
import serverdatamodel.response.SResNewRacerInfo;
import serverobject.ServerGameMaster;
import serverobject.ServerRacerObject;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerCSocketThread implements Runnable{
    private int cSocketID;
    private Socket socketOfServer;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private ServerNetwork.ServerNetworkThread parentThread;

    public ServerCSocketThread(Socket _socketOfServer, int _cSocketID, ServerNetwork.ServerNetworkThread _parentThread) {
        this.cSocketID = _cSocketID;
        this.socketOfServer = _socketOfServer;
        this.parentThread = _parentThread;
        System.out.println(this.getClass().getSimpleName() + " new connection with client# " + this.cSocketID + " at " + socketOfServer);
    }

    @Override
    public void run() {
        try {
            // Server socket I/O
            inStream = new DataInputStream(socketOfServer.getInputStream());
            outStream = new DataOutputStream(socketOfServer.getOutputStream());

            while (true) {
                int cmd = inStream.readInt();
                if (cmd == ServerNetworkConfig.CMD.DISCONNECT) {
                    break;
                }

                int lData = inStream.available();
                byte[] bytes = new byte[lData];
                inStream.read(bytes);

                // Switch on command id
                switch (cmd) {
                    case ServerNetworkConfig.CMD.CMD_LOGIN:
                        handleLogin(cmd, bytes, this.outStream, this.parentThread);
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
                            System.out.println(this.getClass().getSimpleName() + ": online status " + isOnline);
                            if (isOnline == 1) {
                                System.out.println(this.getClass().getSimpleName() + ": duplicated login");

                                SResLoginError sResLoginError = new SResLoginError(cmd, ServerNetworkConfig.LOGIN_FLAG.DUPLICATED_LOGIN);
                                outStream.write(sResLoginError.pack());
                            } else {
                                // create existing user, set isonline, send individually (success login) and bulk (update number of racers to all)
                                System.out.println(this.getClass().getSimpleName() + ": exist user");

                                int victory = user.getInt(ServerDBConfig.TABLE_RACER_victory);
                                ServerRacerObject sRacer = new ServerRacerObject(sReqAccount.getUsername(), sReqAccount.getPassword(), victory);
                                ServerGameMaster.getInstance().addSRacer(sRacer);

                                String updateUser = "UPDATE " + ServerDBConfig.TABLE_RACER + " SET " + ServerDBConfig.TABLE_RACER_isonline + " = 1 WHERE " + ServerDBConfig.TABLE_RACER_username + " = '"+ sReqAccount.getUsername() + "'";
                                ServerDBHelper.getInstance().exec(updateUser);

                                SResLoginSuccess sResLoginSuccess = new SResLoginSuccess(cmd, ServerNetworkConfig.LOGIN_FLAG.SUCCESS, sReqAccount.getUsername(), victory, ServerGameMaster.getInstance());
                                outStream.write(sResLoginSuccess.pack());

                                SResNewRacerInfo sResNewRacerInfo = new SResNewRacerInfo(ServerNetworkConfig.CMD.CMD_INFO, ServerNetworkConfig.INFO_TYPE_FLAG.TYPE_NOTICE_NEW_RACER, sReqAccount.getUsername(), ServerGameMaster.getInstance());
                                this.parentThread.signalAllClients(sResNewRacerInfo, this.cSocketID, true);
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
                    // if it is not, create new user, record database, send individually (success login) and bulk (update number of racers to all)
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

                    SResLoginSuccess sResLoginSuccess = new SResLoginSuccess(cmd, ServerNetworkConfig.LOGIN_FLAG.SUCCESS, sReqAccount.getUsername(), victory, ServerGameMaster.getInstance());
                    outStream.write(sResLoginSuccess.pack());

                    SResNewRacerInfo sResNewRacerInfo = new SResNewRacerInfo(ServerNetworkConfig.CMD.CMD_INFO, ServerNetworkConfig.INFO_TYPE_FLAG.TYPE_NOTICE_NEW_RACER, sReqAccount.getUsername(), ServerGameMaster.getInstance());
                    this.parentThread.signalAllClients(sResNewRacerInfo, this.cSocketID, true);
                }
            }
        }
        else {
            // if no, not record login, send individually (no more slots)
            SResLoginError sResLoginError = new SResLoginError(cmd, ServerNetworkConfig.LOGIN_FLAG.NO_MORE_SLOTS);
            outStream.write(sResLoginError.pack());
        }
    }

    public void reply (ServerDataModel data) {
        try {
            outStream.write(data.pack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
