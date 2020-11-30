package clientnetwork;

import clientGUI.ClientGUI;

import clientdatamodel.receive.CRecAllRacersInfo;
import clientdatamodel.receive.CRecLogin;
import clientdatamodel.receive.CRecOpponentInfo;

import clientdatamodel.receive.CRecQuestion;
import clientobject.*;

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
                    case ClientNetworkConfig.CMD.CMD_QUESTION:
                        receiveQuestion(bytes);
                        break;
                    case ClientNetworkConfig.CMD.CMD_RESULT:
                        receiveResult(bytes);
                        break;

                    default:
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

    private void receiveQuestion(byte[] bytes) {
        CRecQuestion cRecQuestion = new CRecQuestion();
        cRecQuestion.unpack(bytes);

        // update UI
        ClientQuestion currentQuestion = new ClientQuestion(
                cRecQuestion.getCQuestionID(),
                cRecQuestion.getCNum1(),
                cRecQuestion.getCOp(),
                cRecQuestion.getCNum2(),
                cRecQuestion.getTimeOffset()
        );
        ClientGameMaster.getInstance().setCurrentQuestion(currentQuestion);
    }

    private void receiveLogin(byte[] bytes) throws InterruptedException {
        CRecLogin cRecLogin = new CRecLogin();
        cRecLogin.unpack(bytes);

        switch (cRecLogin.getEventFlag()) {
            case ClientNetworkConfig.LOGIN_FLAG.NO_MORE_SLOTS:
                System.out.println(getClass().getSimpleName() + ": NO_MORE_SLOTS");
                // update UI
                break;
            case ClientNetworkConfig.LOGIN_FLAG.USERNAME_TAKEN:
                System.out.println(getClass().getSimpleName() + ": USERNAME_TAKEN");
                // update UI
                break;
            case ClientNetworkConfig.LOGIN_FLAG.DUPLICATED_LOGIN:
                System.out.println(getClass().getSimpleName() + ": DUPLICATED_LOGIN");
                // update UI
                ClientGUI.getInstance().setJoinServerNoti("Duplicated Login ", 0);
                break;
            case ClientNetworkConfig.LOGIN_FLAG.ERROR:
                System.out.println(getClass().getSimpleName() + ": ERROR");
                // update UI
                break;
            case ClientNetworkConfig.LOGIN_FLAG.SUCCESS:
                System.out.println(getClass().getSimpleName() + ": SUCCESS");

                // confirm this racer ==> means local input username and password are accepted
                ClientGameMaster.getInstance().confirmRacerPostLogin(cRecLogin.getRacerVictory());
                ClientGUI.getInstance().setJoinServerNoti("Login Successfully ", 9);

                // record his opponent array
                ClientGameMaster.getInstance().setNumOfRacers(cRecLogin.getNumOfRacers());
                ClientGameMaster.getInstance().setInitCOpponents(cRecLogin.getcOpponents());

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
        ClientPlayer clientOpponent = new ClientPlayer(info.getOpponentUsername(), info.getOpponentPosition(), 0, info.getOpponentStatus(), "");
        ClientGameMaster.getInstance().addNewOpponent(clientOpponent);
    }

    private void _ROI_updateOpponentInfo (CRecOpponentInfo info) {
        // updated a racer
        ClientPlayer clientOpponent = new ClientPlayer(info.getOpponentUsername(), info.getOpponentPosition(), 0, info.getOpponentStatus(), "");
        ClientGameMaster.getInstance().updateAnOpponent(clientOpponent);
    }

    private void receiveResult(byte[] bytes) {
        CRecAllRacersInfo cRecAllRacersInfo = new CRecAllRacersInfo();
        cRecAllRacersInfo.unpack(bytes);

        this._RR_updateThisRacer(cRecAllRacersInfo);

        this._RR_updateOpponentsInfo(cRecAllRacersInfo);
    }

    private void _RR_updateThisRacer (CRecAllRacersInfo cRecAllRacersInfo) {
        // update this racer info
        ClientRacer thisRacer = ClientGameMaster.getInstance().getCRacer();
        ClientPlayer thisPlayer = cRecAllRacersInfo.getThisRacer(thisRacer.getNickname());

        thisRacer.setStatusFlag(thisPlayer.getStatusFlag());

        int newPositionOfThisRacer = thisPlayer.getPosition();
        thisRacer.setGain(newPositionOfThisRacer - thisRacer.getPosition());
        thisRacer.setPosition(newPositionOfThisRacer);

        // signal the master to update its racer with these info
        ClientGameMaster.getInstance().updateThisRacer(thisRacer.getNickname());
    }

    private void _RR_updateOpponentsInfo(CRecAllRacersInfo cRecAllRacersInfo) {
        ClientGameMaster.getInstance().updateAllOpponents(cRecAllRacersInfo.getAllRacers());
    }

}
