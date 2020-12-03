package serverGUI;

import servernetwork.ServerNetwork;
import serverobject.ServerGameConfig;
import serverobject.ServerGameMaster;
import serverobject.ServerRacerObject;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;

import java.awt.event.*;
import java.io.IOException;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ServerGUI extends JFrame {
    private JPanel ServerPanel;

    private JLabel questionSectionLabel;

    private JLabel gameConfigLabel;
    private JLabel numOfRacersLabel;
    private JSpinner numOfRacersSpinner;
    private JLabel raceLengthLabel;
    private JSpinner raceLengthSpinner;

    private JLabel connectionNoti;

    private JLabel openConnectionWarning;
    private JButton openConnectionButton;
    private JButton startGameButton;

    private JLabel gameControlLabel;
    private JLabel numOfPplJoiningLabel;
    private JLabel numOfPplJoining;

    private JSeparator separator1, separator2, separator3;

    public JTable racerStatTable;
    private JLabel racerStatLabel;
    private JScrollPane statTableScrollPane;

    private JLabel questionLabel;
    private JLabel firstNum, operator, secondNum;
    private JLabel correctAnsLabel;
    private JLabel updateCorrectAns;
    private JLabel timerLabel;
    private JLabel updateTimer;

    private JLabel winnerLabel;
    private JLabel winner;
    private JLabel image;

    private JSeparator verticalSeparator1, verticalSeparator2, verticalSeparator3, verticalSeparator4;

    DefaultTableModel dtm;

    private HashMap<Integer, ActionListener> startGameButtonlisteners;

    // Singleton
    private static ServerGUI serverGUI = null;
    public static ServerGUI getInstance() {
        if (serverGUI == null) {
            serverGUI = new ServerGUI(ServerGUIConfig.GAME_NAME);
        }
        return serverGUI;
    }

    public ServerGUI(String _gameName) {
        super(_gameName);
        serverGUI = this;

        this.startGameButtonlisteners = new HashMap<>();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setContentPane(ServerPanel);
        this.setServerGUI();
    }

    // create table UI
    // this is a default function, therefore, have to use this name for the function
    private void createUIComponents() {
        dtm = new DefaultTableModel(null, ServerGUIConfig.TABLE_COLS);
        dtm.setColumnIdentifiers(ServerGUIConfig.TABLE_COLS);

        racerStatTable = new JTable(dtm) {
            @Override
            public Class getColumnClass(int column) { // return column class
                return (column == 0) ? Icon.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) { // turn off cell modification
                return false;
            }
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) { // interchange background color for each row
                Component c = super.prepareRenderer(renderer, row, column);
                if (!c.getBackground().equals(getSelectionBackground())) {
                    c .setBackground(row % 2 == 0 ? Color.WHITE : ServerGUIConfig.BORDER_COLOR);
                }
                return c;
            }
        };

        statTableScrollPane = new JScrollPane(racerStatTable);
    }

    private void setServerGUI() {
        // set icon
        try {
            ServerGUI.getInstance().setIconImage(ImageIO.read(this.getClass().getResource("assets/dog-russel-grin-icon.png")));
            image.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("assets/background.png")).getScaledInstance(216, 115, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            System.err.println("Cannot set icon for Server UI");
            e.printStackTrace();
        }

        // set panel
        this.getContentPane().setBackground(ServerGUIConfig.BACKGROUND_COLOR);

        // set label
        setLabelUI();

        // set spinner
        setSpinnerUI();

        // set button
        setButtonUI();

        // set separator
        setSeparatorUI();

        // set table
        setTableUI();
    }

    private void setLabelUI() {
        numOfRacersLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        raceLengthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        numOfPplJoiningLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        connectionNoti.setForeground(Color.RED);
        connectionNoti.setFont(new Font("Arial", Font.ITALIC, 10));

        gameConfigLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 16));
        gameControlLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 16));

        winnerLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 16));
        winner.setFont(new Font("Arial", Font.ITALIC, 9));
        winner.setText("Unknown.");

        questionSectionLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));
        racerStatLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));

        openConnectionWarning.setFont(new Font("Arial", Font.ITALIC, 9));
        openConnectionWarning.setForeground(Color.RED);
    }

    private void setSpinnerUI() {
        numOfRacersSpinner.setModel(new SpinnerNumberModel(ServerGameConfig.INIT_NUM_OF_RACERS, ServerGameConfig.MIN_NUM_OF_RACERS, ServerGameConfig.MAX_NUM_OF_RACERS, 1));
        raceLengthSpinner.setModel(new SpinnerNumberModel(ServerGameConfig.INIT_RACE_LENGTH, ServerGameConfig.MIN_RACE_LENGTH, ServerGameConfig.MAX_RACE_LENGTH, 1));

        numOfRacersSpinner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        raceLengthSpinner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // update number of racers in server
        numOfRacersSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ServerGameMaster.getInstance().setNumOfRacers((int)numOfRacersSpinner.getValue());
            }
        });

        // update race length in server
        raceLengthSpinner.addChangeListener(e -> ServerGameMaster.getInstance().setRaceLength((int) raceLengthSpinner.getValue()));

        JFormattedTextField numOfRacersTextField = ((JSpinner.DefaultEditor)numOfRacersSpinner.getEditor()).getTextField();
        numOfRacersTextField.setEditable(false);
        numOfRacersTextField.setHorizontalAlignment(SwingConstants.CENTER);

        JFormattedTextField raceLengthTextField = ((JSpinner.DefaultEditor)raceLengthSpinner.getEditor()).getTextField();
        raceLengthTextField.setEditable(false);
        raceLengthTextField.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setButtonUI() {
        openConnectionButton.setBackground(ServerGUIConfig.LIGHT_GREEN);
        openConnectionButton.setBorder(new LineBorder(ServerGUIConfig.LIGHT_GREEN));

        openConnectionButton.addActionListener(e -> {
            openConnectionButton.setEnabled(false); // can no longer click the button

            ServerNetwork.getInstance().openServerSocket(); // open server socket and connect to database

            connectionNoti.setForeground(ServerGUIConfig.LIGHT_GREEN);
            connectionNoti.setText("Connection Open "); // show text to notify that server has opened

            disableComponentAfterOpenConnection(); // disable changeability of configuration
            setTableUI();
        });

        startGameButton.setBackground(ServerGUIConfig.LIGHT_GREEN);
        startGameButton.setBorder(new LineBorder(ServerGUIConfig.LIGHT_GREEN));
        startGameButton.setEnabled(false);

        ActionListener alGiveQuestion = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServerGameMaster.getInstance().giveQuestion();
            }
        };

        ActionListener alReplay = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServerGameMaster.getInstance().replay();
                startGameButton.setText("GIVE QUESTION");

                startGameButton.setBorder(new LineBorder(ServerGUIConfig.LIGHT_GREEN));
                startGameButton.setBackground(ServerGUIConfig.LIGHT_GREEN);
                startGameButton.setForeground(Color.WHITE);

                startGameButton.removeActionListener(startGameButtonlisteners.get(1));
                startGameButton.addActionListener(startGameButtonlisteners.get(0));
            }
        };
        startGameButton.addActionListener(alGiveQuestion);

        startGameButtonlisteners.put(0, alGiveQuestion);
        startGameButtonlisteners.put(1, alReplay);
    }

    private void setSeparatorUI() {
        List<JSeparator> hSep = Arrays.asList(separator1, separator2, separator3);

        for (int i = 0; i < hSep.size(); ++i) {
            hSep.get(i).setBackground(ServerGUIConfig.BORDER_COLOR);
            hSep.get(i).setForeground(ServerGUIConfig.BORDER_COLOR);
            hSep.get(i).setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, ServerGUIConfig.BORDER_COLOR));
        }

        List<JSeparator> vSep = Arrays.asList(verticalSeparator1, verticalSeparator2, verticalSeparator3, verticalSeparator4);

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

        int height = (ServerGameMaster.getInstance().getNumOfRacers() + 1) * ServerGUIConfig.ROW_HEIGHT;
        int width = 0;
        for (int i = 0; i < ServerGUIConfig.PREFERRED_WIDTH.length; ++i) { width += ServerGUIConfig.PREFERRED_WIDTH[i]; }
        statTableScrollPane.setPreferredSize(new Dimension(width, height));
        statTableScrollPane.setMaximumSize(new Dimension(width, height));

        // set table: row height, empty border, no horizontal line
        racerStatTable.setRowHeight(ServerGUIConfig.ROW_HEIGHT);
        racerStatTable.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        racerStatTable.setShowHorizontalLines(false);

        // set table's header: no border, row height, bg color, text color, text font
        UIManager.getDefaults().put("TableHeader.cellBorder", BorderFactory.createEmptyBorder(0,0,0,0));
        racerStatTable.getTableHeader().setPreferredSize(new Dimension(-1, ServerGUIConfig.ROW_HEIGHT-1));
        racerStatTable.getTableHeader().setBackground(Color.BLACK);
        racerStatTable.getTableHeader().setForeground(Color.WHITE);
        racerStatTable.getTableHeader().setFont(new Font("Britannic Bold", Font.PLAIN, 12));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < ServerGUIConfig.TABLE_COLS.length; ++i) {
            racerStatTable.getColumnModel().getColumn(i).setMaxWidth(ServerGUIConfig.PREFERRED_WIDTH[i]); // set column width
            racerStatTable.getColumnModel().getColumn(i).setCellRenderer(center); // align center text in each cell
        }
    }

    private String strikeThroughText(String str) {
        return "<HTML><STRIKE>" + str + "</STRIKE></HTML>";
    }

    private String removeStrikeThrough(String str) {
        str = str.replace("<HTML><STRIKE>", "");
        str = str.replace("</STRIKE></HTML>", "");
        return str;
    }

    private Boolean checkIfEqualToNickname(String str, String nickname) {
        if (str.equals(nickname) || str.equals("<HTML><STRIKE>" + nickname + "</STRIKE></HTML>")) {
            return true;
        }
        return false;
    }

    private void disableComponentAfterOpenConnection() {
        numOfRacersSpinner.setEnabled(false);
        raceLengthSpinner.setEnabled(false);
        openConnectionButton.setEnabled(false);
    }

    public void updateControllButtonToReplayButton() {
        startGameButton.setText("REPLAY");
        startGameButton.setBackground(Color.RED);
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setBorder(new LineBorder(Color.RED));
    }

    public void changeStateOfControllButton() {
        startGameButton.removeActionListener(startGameButtonlisteners.get(0));
        startGameButton.addActionListener(startGameButtonlisteners.get(1));
    }

    public void addSRacerToUI(String racerName, int gain, int status, int position) {
        String gainStr = gain >= 0 ? ("+"+gain) : String.valueOf(gain);
        dtm.addRow(new Object[]{racerName, "Empty", gainStr, ServerGameConfig.STATUS_STRING[status], position});
    }

    public void updateSRacerToUI(String racerName, int gain, int status, int position) {
        String gainStr = gain >= 0 ? ("+"+String.valueOf(gain)) : String.valueOf(gain);

        for (int i = 0; i < ServerGameMaster.getInstance().getNumOfRacers(); ++i) {
            String str = (String)dtm.getValueAt(i, 0);

            if (checkIfEqualToNickname(str, racerName)) {
                dtm.setValueAt(gainStr, i, 2);
                dtm.setValueAt(ServerGameConfig.STATUS_STRING[status], i, 3);
                dtm.setValueAt(position, i, 4);

                System.out.println("ServerGUI340 "+ dtm.getValueAt(i, 2) + " " + dtm.getValueAt(i, 3) + " " + dtm.getValueAt(i, 2));
            }
        }
    }

    public void updateSRacerAnswerToUI(String racerName, int answer) {
        for (int i = 0; i < ServerGameMaster.getInstance().getNumOfRacers(); ++i) {
            String str = (String)dtm.getValueAt(i, 0);

            if (checkIfEqualToNickname(str, racerName)) {
                if (answer == Integer.MAX_VALUE) {
                    dtm.setValueAt("Empty", i, 1);
                }
                else {
                    dtm.setValueAt(answer, i, 1);
                }
            }
        }
    }

    public void removeSRacerFromUI(String racerName) {
        for (int i = 0; i < ServerGameMaster.getInstance().getNumOfRacers(); ++i) {
            String str = (String)dtm.getValueAt(i, 0);

            if (checkIfEqualToNickname(str, racerName)) {
                dtm.removeRow(i);
                break;
            }
        }
    }

    public void updateNumOfPplJoiningValue(int i) {
        numOfPplJoining.setText(Integer.toString(i));

        // if number of ppl join equal number of racers config then enable start game button
        if (numOfPplJoining.getText().equals(numOfRacersSpinner.getValue().toString())) {
            startGameButton.setEnabled(true);
        }
        else {
            startGameButton.setEnabled(false);
        }
    }

    public void setFirstNum(int firstNum) { this.firstNum.setText(Integer.toString(firstNum)); }
    public void setSecondNum(int secondNum) { this.secondNum.setText(Integer.toString(secondNum)); }
    public void setOperator(int operator) { this.operator.setText(ServerGameConfig.OPERATORS[operator]); }
    public void setAnswer(int answer) { this.updateCorrectAns.setText(Integer.toString(answer)); }

    public void setUpdateTimer(int time) { this.updateTimer.setText(Integer.toString(time)); }

    // strike through name of whom is eliminated from the race
    public void strikeThroughEliminatedRacer(String racerName) {
        for (int i = 0; i < ServerGameMaster.getInstance().getNumOfRacers(); ++i) {
            String str = (String)dtm.getValueAt(i, 0);

            if (checkIfEqualToNickname(str, racerName)) {
                dtm.setValueAt(strikeThroughText((String)dtm.getValueAt(i, 0)), i, 0);
            }
        }
    }

    public void announceWinner(String winnerName) {
        winner.setFont(new Font("Britannic Bold", Font.PLAIN, 25));
        winner.setText(winnerName);
    }

    public void announceNoWinner() {
        winner.setFont(new Font("Arial", Font.ITALIC, 9));
        winner.setText("Ppl in this server are not so smart :> ");
    }

    public void resetUIForReplay() {
        // reset question info
        firstNum.setText("1st no.");
        operator.setText("op");
        secondNum.setText("2nd no.");
        setAnswer(0);

        // reset winner
        winner.setFont(new Font("Arial", Font.ITALIC, 9));
        winner.setText("Unknown.");
    }

    public void renewRacerNickname(ServerRacerObject racer) {
        for (int i = 0; i < ServerGameMaster.getInstance().getNumOfRacers(); ++i) {
            String str = (String)dtm.getValueAt(i, 0);

            if (checkIfEqualToNickname(str, racer.getUsername())) {
                dtm.setValueAt(removeStrikeThrough(str), i, 0);
            }
        }
    }

    public void setGiveQuestionButton(boolean bool) {
        startGameButton.setEnabled(bool);
    }
}
