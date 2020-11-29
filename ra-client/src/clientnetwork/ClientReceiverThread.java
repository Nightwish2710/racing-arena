package clientnetwork;

import clientGUI.ClientGUI;
import clientGUI.ClientGUIConfig;
import clientdatamodel.receive.CRecLogin;
import clientdatamodel.receive.CRecOpponentInfo;
import clientobject.ClientGameMaster;
import clientobject.ClientOpponent;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;


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
            } catch (IOException e) {
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

    private void receiveLogin(byte[] bytes) {
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
                // confirm this racer, record his opponent array
                ClientGameMaster.getInstance().getcRacer().setNumOfVictory(cRecLogin.getRacerVictory());
                ClientGameMaster.getInstance().setNumOfRacers(cRecLogin.getNumOfRacers());
                ClientGameMaster.getInstance().setCurentNumOfRacers(cRecLogin.getCurrentNumOfRacers());
                ClientGameMaster.getInstance().setcOpponents(cRecLogin.getcOpponents());

                // update UI
                ClientGUI.getInstance().disableComponentAfterJoinServer();

                ClientGUI.getInstance().setRacerStatusPanelFlag(ClientGUIConfig.ACTION_ON_RACER_STATUS_PANEL_FLAG.INIT_OPPONENT_BAR_FLAG);
                ClientGUI.getInstance().createUIComponents();

                for (Map.Entry<String, ClientOpponent> opps : cRecLogin.getcOpponents().entrySet()) {
                    System.out.println(getClass().getSimpleName() + " got: " + opps.getKey() + " - " + opps.getValue().getStatusFlag());
                }

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
            default:
                break;
        }
    }

    private void _ROI_newOpponentInfo (CRecOpponentInfo info) {
        // added new racer
        System.out.println(getClass().getSimpleName() + ": new opponent info: ");
        System.out.println(getClass().getSimpleName() + info.getOpponentUsername());
        System.out.println(getClass().getSimpleName() + info.getOpponentPosition());
        System.out.println(getClass().getSimpleName() + info.getOpponentStatus());
        System.out.println(getClass().getSimpleName() + info.getCurrentNumOfRacers());
    }
}
