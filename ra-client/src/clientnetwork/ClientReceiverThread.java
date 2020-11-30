package clientnetwork;

import clientGUI.ClientGUI;

import clientdatamodel.receive.CRecLogin;
import clientdatamodel.receive.CRecOpponentInfo;

import clientobject.ClientGameMaster;
import clientobject.ClientOpponent;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceiverThread implements Runnable {
    private boolean isPermittedToRun;
    private Socket clientSocket;
    private DataInputStream inStream;

    public ClientReceiverThread(Socket _clientSocket, DataInputStream _inStream) {
        this.isPermittedToRun = true;
        this.clientSocket = _clientSocket;
        this.inStream = _inStream;
    }

    @Override
    public void run() {
        while (this.isPermittedToRun) {
            if (this.clientSocket.isClosed()) {
                return;
            }

            try {
                int cmd = this.inStream.readInt();

                int lData = this.inStream.available();
                byte[] bytes = new byte[lData];
                inStream.read(bytes);

                // Switch on command id
                switch (cmd) {
                    case ClientNetworkConfig.CMD.CMD_LOGIN:
                        receiveLogin(bytes);
                        break;

                    case ClientNetworkConfig.CMD.CMD_INFO:
                        receiveOpponentInfo(bytes);
                        break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void stopReceiverThread() throws IOException {
        this.isPermittedToRun = false;
        this.inStream.close();
        this.clientSocket.close();
    }

    private void receiveLogin(byte[] bytes) throws InterruptedException {
        CRecLogin cRecLogin = new CRecLogin();
        cRecLogin.unpack(bytes);

        switch (cRecLogin.getEventFlag()) {
            case ClientNetworkConfig.LOGIN_FLAG.NO_MORE_SLOTS:
                // update UI
                System.out.println(getClass().getSimpleName() + ": NO_MORE_SLOTS");
                break;
            case ClientNetworkConfig.LOGIN_FLAG.USERNAME_TAKEN:
                // update UI
                System.out.println(getClass().getSimpleName() + ": USERNAME_TAKEN");
                break;
            case ClientNetworkConfig.LOGIN_FLAG.DUPLICATED_LOGIN:
                // update UI
                System.out.println(getClass().getSimpleName() + ": DUPLICATED_LOGIN");
                break;
            case ClientNetworkConfig.LOGIN_FLAG.ERROR:
                // update UI
                System.out.println(getClass().getSimpleName() + ": ERROR");
                break;
            case ClientNetworkConfig.LOGIN_FLAG.SUCCESS:
                // confirm this racer ==> means local input username and password are accepted
                ClientGameMaster.getInstance().confirmRacerPostLogin(cRecLogin.getRacerVictory());
                ClientGUI.getInstance().setJoinServerNoti("Success");

                // record his opponent array
                ClientGameMaster.getInstance().setNumOfRacers(cRecLogin.getNumOfRacers());
                ClientGameMaster.getInstance().setInitCOpponents(cRecLogin.getcOpponents());

//                System.out.println("START ANSWERING");
//                ClientGUI.getInstance().startAnswering();
//                System.out.println("STOP ANSWERING");
//                ClientGUI.getInstance().stopAnswering();

                // lock connection button and text area for nickname and password
                ClientGUI.getInstance().disableComponentAfterJoinServer();
                break;
            
            default:
                break;
        }
    }

    private void receiveOpponentInfo(byte[] bytes) {
        CRecOpponentInfo cRecOpponentInfo = new CRecOpponentInfo();
        cRecOpponentInfo.unpack(bytes);

        switch (cRecOpponentInfo.getEventFlag()) {
            case ClientNetworkConfig.INFO_TYPE_FLAG.TYPE_NOTICE_NEW_OPPONENT:
                _ROI_newOpponentInfo(cRecOpponentInfo);
                break;
            case ClientNetworkConfig.INFO_TYPE_FLAG.TYPE_NOTICE_UPDATE_OPPONENT:
                _ROI_updateOpponentInfo(cRecOpponentInfo);
                break;
            default:
                break;
        }
    }

    private void _ROI_newOpponentInfo (CRecOpponentInfo info) {
        // added new racer
        ClientOpponent clientOpponent = new ClientOpponent(info.getOpponentUsername(), info.getOpponentPosition(), 0, info.getOpponentStatus(), "");
        ClientGameMaster.getInstance().addNewOpponent(clientOpponent);
    }

    private void _ROI_updateOpponentInfo (CRecOpponentInfo info) {
        // updated a racer
        ClientOpponent clientOpponent = new ClientOpponent(info.getOpponentUsername(), info.getOpponentPosition(), 0, info.getOpponentStatus(), "");
        ClientGameMaster.getInstance().updateAnOpponent(clientOpponent);
    }
}
