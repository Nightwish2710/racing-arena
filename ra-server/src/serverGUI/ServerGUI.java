package serverGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServerGUI extends JFrame {
    private static String[] tableHeader = {"Order", "Racer", "Gain", "Status", "Points", "Position"};

    private JPanel ServerPanel;

    private JPanel serverLogsPanel;
    private JLabel serverLogsPanelLabel;

    private JLabel gameConfigLabel;
    private JLabel numOfRacersLabel;
    private JSpinner numOfRacersSpinner;
    private JLabel raceLengthLabel;
    private JTextField enterRaceLength;

    private JButton resetButton;
    private JButton setButton;

    private JLabel openConnectionWarning;
    private JButton openConnectionButton;
    private JButton startGameButton;

    private JLabel gameControlLabel;
    private JLabel numOfPplJoiningLabel;
    private JLabel numOfPplJoining;

    private JSeparator separator1;

    private JTable racerStatusTable;

    public ServerGUI(String gameName) {
        super(gameName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(ServerPanel);
        this.setServerGUI();

        this.pack();
    }

    private void setServerGUI() {
        // set panel
        ServerPanel.setBackground(ServerGUIConfig.BACKGROUND_COLOR);

        // set label
        numOfRacersLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        raceLengthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        numOfPplJoiningLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        gameConfigLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 16));
        gameControlLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 16));

        serverLogsPanelLabel.setForeground(ServerGUIConfig.ACCENT_COLOR);
        serverLogsPanelLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));

        openConnectionWarning.setFont(new Font("Arial", Font.ITALIC, 9));
        openConnectionWarning.setForeground(Color.RED);

        // set table
        setTable();

        // set server panel
        serverLogsPanel.setOpaque(true);
        serverLogsPanel.setBackground(ServerGUIConfig.BORDER_COLOR);
    }

    private void setTable() {
        DefaultTableModel dtm = new DefaultTableModel(null, tableHeader) {
            @Override
            public Class<?> getColumnClass(int col) {
                return getValueAt(0, col).getClass();
            }
        };
        JTable racerStatusTable = new JTable(dtm);
    }
}
