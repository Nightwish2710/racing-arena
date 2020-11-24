package clientnetwork;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReceiverThread extends Thread {

    private BufferedReader inStream;
    private String responseLine;
    public ClientReceiverThread(BufferedReader _inStream) {
        this.inStream = _inStream;
        responseLine = null;
    }

    @Override
    public void run() {
        try {
            responseLine = inStream.readLine();
            if (responseLine != null) {
                System.out.println(this.getClass().getSimpleName() + "server says: " + responseLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
