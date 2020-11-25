package serverGUI;

import javax.swing.*;

public class ServerGUI extends JFrame {
    private JPanel ServerPanel;

    private JPanel serverLogsPanel;
    private JLabel serverLogsPanelLabel;

    public ServerGUI(String gameName) {
        super(gameName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(ServerPanel);
        this.pack();
    }
}
