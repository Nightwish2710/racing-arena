package clientGUI;

import javax.swing.*;

public class RacingArenaClient extends JFrame {
    private JPanel ClientPanel;

    private JLabel nickname;
    private JTextField enterNickname;
    private JLabel password;
    private JPasswordField enterPassword;
    private JButton joinServer;

    private JTabbedPane serverResponse;

    private JLabel position;
    private JLabel updatePosition;
    private JLabel points;
    private JLabel updatePoint;

    private JLabel question;
    private JTextField enterAnswer;
    private JButton sendAnswer;
    private JLabel updateQuestion;
    private JProgressBar timer;


    public RacingArenaClient(String gameName) {
        super(gameName);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(ClientPanel);
        this.pack();
    }
}
