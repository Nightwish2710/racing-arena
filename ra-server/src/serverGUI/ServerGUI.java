package serverGUI;

import servernetwork.ServerNetwork;
import serverobject.ServerGameConfig;
import serverobject.ServerRefereeObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.util.Arrays;
import java.util.List;
import java.util.regex.*;

public class ServerGUI extends JFrame {
    private JPanel ServerPanel;

    private JPanel serverLogsPanel;
    private JLabel serverLogsPanelLabel;

    private JLabel gameConfigLabel;
    private JLabel numOfRacersLabel;
    private JSpinner numOfRacersSpinner;
    private JLabel raceLengthLabel;
    private JSpinner raceLengthSpinner;

    private JLabel openConnectionWarning;
    private JButton openConnectionButton;
    private JButton startGameButton;

    private JLabel gameControlLabel;
    private JLabel numOfPplJoiningLabel;
    private JLabel numOfPplJoining;

    private JSeparator separator1, separator2;
    private List<JSeparator> hSep = Arrays.asList(separator1, separator2);

    public JTable racerStatTable;
    private JLabel racerStatLabel;
    private JScrollPane statTableScrollPane;

    // Singleton
    private static ServerGUI serverGUI = null;

    public static ServerGUI getInstance() {
        if (serverGUI == null) {
            serverGUI = new ServerGUI(ServerGUIConfig.GAME_NAME);
            serverGUI.setLocationRelativeTo(null);
            serverGUI.setVisible(true);
        }
        return serverGUI;
    }
    private JLabel questionLabel;
    private JLabel firstNumer, operant, secondNumber;
    private JLabel correctAnsLabel;
    private JLabel updateCorrectAns;

    private JSeparator verticalSeparator1, verticalSeparator2, verticalSeparator3;
    private List<JSeparator> vSep = Arrays.asList(verticalSeparator1, verticalSeparator2, verticalSeparator3);

    public ServerGUI(String gameName) {
        super(gameName);
        serverGUI = this;
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

        serverLogsPanelLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));
        racerStatLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));

        openConnectionWarning.setFont(new Font("Arial", Font.ITALIC, 9));
        openConnectionWarning.setForeground(Color.RED);

        // set spinner
        setSpinnerUI();

        // set button
        openConnectionButton.setBackground(ServerGUIConfig.LIGHT_GREEN);
        openConnectionButton.setBorder(new LineBorder(ServerGUIConfig.LIGHT_GREEN));
        openConnectionButton.addActionListener(actionOpenConnection);

        startGameButton.setBackground(ServerGUIConfig.LIGHT_GREEN);
        startGameButton.setBorder(new LineBorder(ServerGUIConfig.LIGHT_GREEN));

        // set separator
        setSeparatorUI();

        // set table
        setTableUI();

        // set server panel
        serverLogsPanel.setOpaque(true);
        serverLogsPanel.setBackground(ServerGUIConfig.BORDER_COLOR);
    }

    private void setSpinnerUI() {
        numOfRacersSpinner.setModel(new SpinnerNumberModel(6, ServerGameConfig.MIN_NUM_OF_RACER, ServerGameConfig.MAX_NUM_OF_RACER, 1));
        raceLengthSpinner.setModel(new SpinnerNumberModel(15, ServerGameConfig.MIN_RACE_LENGTH, ServerGameConfig.MAX_RACE_LENGTH, 1));

        numOfRacersSpinner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        raceLengthSpinner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // update number of racers in server
        numOfRacersSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ServerRefereeObject.getInstance().setNumberOfRacer((int)numOfRacersSpinner.getValue());
            }
        });

        // update race length in server
        raceLengthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ServerRefereeObject.getInstance().setRaceLength((int) raceLengthSpinner.getValue());
            }
        });

        JFormattedTextField numOfRacersTextField = ((JSpinner.DefaultEditor)numOfRacersSpinner.getEditor()).getTextField();
        numOfRacersTextField.setEditable(false);
        numOfRacersTextField.setHorizontalAlignment(SwingConstants.CENTER);

        JFormattedTextField raceLengthTextField = ((JSpinner.DefaultEditor)raceLengthSpinner.getEditor()).getTextField();
        raceLengthTextField.setEditable(false);
        raceLengthTextField.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setSeparatorUI() {
        for (int i = 0; i < hSep.size(); ++i) {
            hSep.get(i).setBackground(ServerGUIConfig.BORDER_COLOR);
            hSep.get(i).setForeground(ServerGUIConfig.BORDER_COLOR);
            hSep.get(i).setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, ServerGUIConfig.BORDER_COLOR));
        }

        for (int i = 0; i < vSep.size(); ++i) {
            vSep.get(i).setOrientation(SwingConstants.VERTICAL);
            vSep.get(i).setPreferredSize(new Dimension(3, 40));
            vSep.get(i).setBackground(ServerGUIConfig.BORDER_COLOR);
            vSep.get(i).setForeground(ServerGUIConfig.BORDER_COLOR);
            vSep.get(i).setBorder(BorderFactory.createMatteBorder(0, 1, 0, 2, ServerGUIConfig.BORDER_COLOR));
        }
    }

    private void setTableUI() {
        // set scroll pane
        statTableScrollPane.setViewportBorder(null);
        statTableScrollPane.getVerticalScrollBar().setBorder(null);
        statTableScrollPane.getHorizontalScrollBar().setBorder(null);
        statTableScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        int height = (ServerRefereeObject.getInstance().getNumberOfRacer() + 1) * ServerGUIConfig.ROW_HEIGHT;
        statTableScrollPane.setPreferredSize(new Dimension(389, height));

        // set table
        createUIComponents();
    }

    // create table UI
    // this is a default function, therefore, have to use this name for the function
    private void createUIComponents() {
        // create table
        DefaultTableModel dtm = new DefaultTableModel(null, ServerGUIConfig.TABLE_COLS);
        dtm.setColumnIdentifiers(ServerGUIConfig.TABLE_COLS);
        racerStatTable = new JTable(dtm) {
            // return column class
            @Override
            public Class getColumnClass(int column) {
                return (column == 0) ? Icon.class : Object.class;
            }
            // turn off cell modification
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            // interchange background color for each row
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!c.getBackground().equals(getSelectionBackground())) {
                    c .setBackground(row % 2 == 0 ? Color.WHITE : ServerGUIConfig.BORDER_COLOR);
                }
                return c;
            }
        };

        // set table: row height, empty border, no horizontal line
        racerStatTable.setRowHeight(ServerGUIConfig.ROW_HEIGHT);
        racerStatTable.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        racerStatTable.setShowHorizontalLines(false);

        // set table's header: no border, row height, bg color, text color, text font
        UIManager.getDefaults().put("TableHeader.cellBorder", BorderFactory.createEmptyBorder(0,0,0,0));
        racerStatTable.getTableHeader().setPreferredSize(new Dimension(-1, ServerGUIConfig.ROW_HEIGHT-2));
        racerStatTable.getTableHeader().setBackground(Color.BLACK);
        racerStatTable.getTableHeader().setForeground(Color.WHITE);
        racerStatTable.getTableHeader().setFont(new Font("Britannic Bold", Font.PLAIN, 12));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < ServerGUIConfig.TABLE_COLS.length; ++i) {
            racerStatTable.getColumnModel().getColumn(i).setMaxWidth(ServerGUIConfig.PREFERRED_WIDTH[i]); // set column width
            racerStatTable.getColumnModel().getColumn(i).setCellRenderer(center); // align center text in each cell
        }

        dtm.addRow(new Object[]{1,"HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{2, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{3, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{4, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{5, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{6, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{7, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{8, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{9, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{10, "HHHHHHHHHH", "+10", "ELIMINATED", 10});

//        dtm.setValueAt(strikeThroughText((String)dtm.getValueAt(0, 3)), 0, 3);
        dtm.setValueAt(atStarToCurrentLeadingRacer((String)dtm.getValueAt(0, 1)), 0, 1);
//        dtm.setValueAt(removeStarFromPreviouslyLeadingRacer((String)dtm.getValueAt(0, 1)), 0, 1);

    }

    // strike through name of whom is eliminated from the race
    private String strikeThroughText(String str) {
        return "<HTML><STRIKE>" + str + "</STRIKE></HTML>";
    }

    // add star to name of whom is currently leading the race
    private String atStarToCurrentLeadingRacer(String str) {
        return "<HTML><p style=\"color:red;\">&#9733;" + str + "&#9733;</p></HTML>";
    }

    // remove star from name of whom is not leading the race anymore
    private String removeStarFromPreviouslyLeadingRacer(String str) {
        Pattern pattern = Pattern.compile("<HTML><p style=\"color:red;\">&#9733;(\\S+)&#9733;</p></HTML>");
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            String result = matcher.group(1);
            return result;
        }

        return str;
    }

    private ActionListener actionOpenConnection = e -> ServerNetwork.getInstance().openServerSocket();

    public JLabel getNumOfPplJoining() {
        return numOfPplJoining;
    }
}
