package serverGUI;

import java.io.*;

public class ServerConsoleOutput extends OutputStream {
    private StringBuilder buffer;
    private PrintStream out;

    // Singleton
    private static ServerConsoleOutput serverConsoleOutput = null;
    public static ServerConsoleOutput getInstance() {
        if (serverConsoleOutput == null) {
            serverConsoleOutput = new ServerConsoleOutput(System.out);
        }
        return serverConsoleOutput;
    }

    public ServerConsoleOutput(PrintStream _out) {
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
            ServerGUI.getInstance().setConsoleTextArea(buffer.toString());
            buffer.delete(0, buffer.length());
            buffer.append(">> ");
        }

        out.print(c);
    }
}
