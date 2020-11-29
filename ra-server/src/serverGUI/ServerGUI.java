package serverGUI;

import servernetwork.ServerNetwork;
import serverobject.ServerGameConfig;
import serverobject.ServerGameMaster;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;

public class ServerGUI extends JFrame {
    private JPanel ServerPanel;

    private JLabel serverLogsPanelLabel;

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

    private JSeparator separator1, separator2;
    final private List<JSeparator> hSep = Arrays.asList(separator1, separator2);

    public JTable racerStatTable;
    private JLabel racerStatLabel;
    private JScrollPane statTableScrollPane;

    private JLabel questionLabel;
    private JLabel firstNum, operator, secondNum;
    private JLabel correctAnsLabel;
    private JLabel updateCorrectAns;

    private JScrollPane serverLogsPane;
    private JTextArea consoleTextArea;

    private JSeparator verticalSeparator1, verticalSeparator2, verticalSeparator3;
    final private List<JSeparator> vSep = Arrays.asList(verticalSeparator1, verticalSeparator2, verticalSeparator3);

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

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setContentPane(ServerPanel);
        this.setServerGUI();
    }

    private void setServerGUI() {
        // set icon
        try {
            ServerGUI.getInstance().setIconImage(ImageIO.read(new File("assets/dog-russel-grin-icon.png")));
        } catch (IOException e) {
            System.err.println("Cannot set icon for Server UI");
            e.printStackTrace();
        }

        // set panel
        this.getContentPane().setBackground(ServerGUIConfig.BACKGROUND_COLOR);

        // set label
        numOfRacersLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        raceLengthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        numOfPplJoiningLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        connectionNoti.setFont(new Font("Arial", Font.ITALIC, 10));

        gameConfigLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 16));
        gameControlLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 16));

        serverLogsPanelLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));
        racerStatLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));

        openConnectionWarning.setFont(new Font("Arial", Font.ITALIC, 9));
        openConnectionWarning.setForeground(Color.RED);

        // set spinner
        setSpinnerUI();

        // set button
        setButtonUI();

        // set separator
        setSeparatorUI();

        // set table
        setTableUI();

        // set server logs scroll pane
        setServerLogsPaneUI();
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
            connectionNoti.setText("Connection Open "); // show text to notify that server has opened
            disableComponentAfterOpenConnection(); // disable changeability of configuration
            setTableUI();
        });

        startGameButton.setBackground(ServerGUIConfig.LIGHT_GREEN);
        startGameButton.setBorder(new LineBorder(ServerGUIConfig.LIGHT_GREEN));
        startGameButton.setEnabled(false);
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

    private void setServerLogsPaneUI() {
        serverLogsPane.setBorder(BorderFactory.createLineBorder(ServerGUIConfig.BORDER_COLOR, 5));
        serverLogsPane.getViewport().setBackground(ServerGUIConfig.BORDER_COLOR);
        serverLogsPane.setOpaque(true);

        serverLogsPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(ServerGUIConfig.BACKGROUND_COLOR);
                button.setBorder(BorderFactory.createLineBorder(ServerGUIConfig.BACKGROUND_COLOR, 1));
                return button;
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(ServerGUIConfig.BACKGROUND_COLOR);
                button.setBorder(BorderFactory.createLineBorder(ServerGUIConfig.BACKGROUND_COLOR, 1));
                return button;
            }
        });

        consoleTextArea.setEnabled(false);
        consoleTextArea.setBackground(ServerGUIConfig.BORDER_COLOR);
        consoleTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        consoleTextArea.setDisabledTextColor(Color.BLACK);
    }

    private void setTableUI() {
        // set scroll pane
        statTableScrollPane.setViewportBorder(null);
        statTableScrollPane.getVerticalScrollBar().setBorder(null);
        statTableScrollPane.getHorizontalScrollBar().setBorder(null);
        statTableScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        int height, width = -1;
        height = (ServerGameMaster.getInstance().getNumOfRacers() + 1) * ServerGUIConfig.ROW_HEIGHT;
        for (int i = 0; i < ServerGUIConfig.PREFERRED_WIDTH.length; ++i) { width += ServerGUIConfig.PREFERRED_WIDTH[i]; }
        statTableScrollPane.setPreferredSize(new Dimension(width, height));

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

//        dtm.addRow(new Object[]{1,"HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{2, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{3, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{4, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{5, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{6, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{7, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{8, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{9, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
//        dtm.addRow(new Object[]{10, "HHHHHHHHHH", "+10", "ELIMINATED", 10});

//        dtm.setValueAt(strikeThroughText((String)dtm.getValueAt(0, 3)), 0, 3);
//        dtm.setValueAt(atStarToCurrentLeadingRacer((String)dtm.getValueAt(0, 1)), 0, 1);
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

        if (matcher.find()) { return matcher.group(1); }

        return str;
    }

    private void disableComponentAfterOpenConnection() {
        numOfRacersSpinner.setEnabled(false);
        raceLengthSpinner.setEnabled(false);
        openConnectionButton.setEnabled(false);
    }

    private void updateNumOfPplJoiningValue(int i) {
        numOfPplJoining.setText(Integer.toString(i));

        // if number of ppl join equal number of racers config then enable start game button
        if (numOfPplJoining.getText().equals(numOfRacersSpinner.getValue().toString())) {
            startGameButton.setEnabled(true);
        }
    }

    public void setConsoleTextArea(String str) {
        if (EventQueue.isDispatchThread()) {
            consoleTextArea.setText(consoleTextArea.getText() + str);
        }
        else {
            EventQueue.invokeLater(() -> {
                // nothing to add yet
            });

        }
    }
}
