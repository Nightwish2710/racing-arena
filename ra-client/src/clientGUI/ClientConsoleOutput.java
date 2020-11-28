package clientGUI;

import java.io.*;

public class ClientConsoleOutput extends OutputStream {
    private StringBuilder buffer;
    private PrintStream out;

    // Singleton
    private static ClientConsoleOutput clientConsoleOutput = null;
    public static ClientConsoleOutput getInstance() {
        if (clientConsoleOutput == null) {
            clientConsoleOutput = new ClientConsoleOutput(System.out);
        }
        return clientConsoleOutput;
    }

    public ClientConsoleOutput(PrintStream _out) {
        this.buffer = new StringBuilder(128);
        this.out = _out;

        buffer.append(">> ");
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        String value = Character.toString(c);
        buffer.append(value);

        if (value.equals("\n")) {
            ClientGUI.getInstance().setConsoleTextArea(buffer.toString());
            buffer.delete(0, buffer.length());
            buffer.append(">> ");
        }

        out.print(c);
    }
}
