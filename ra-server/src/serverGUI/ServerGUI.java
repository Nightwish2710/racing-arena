package serverGUI;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;

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

    public JTable racerStatTable;
    private JLabel racerStatLabel;
    private JScrollPane statTableScrollPane;

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

        serverLogsPanelLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));
        racerStatLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));

        openConnectionWarning.setFont(new Font("Arial", Font.ITALIC, 9));
        openConnectionWarning.setForeground(Color.RED);

        // set spinner
        setSpinnerUI();

        // set button
        openConnectionButton.setBackground(ServerGUIConfig.LIGHT_GREEN);
        openConnectionButton.setBorder(new LineBorder(ServerGUIConfig.LIGHT_GREEN));

        startGameButton.setBackground(ServerGUIConfig.LIGHT_GREEN);
        startGameButton.setBorder(new LineBorder(ServerGUIConfig.LIGHT_GREEN));

        // set separator
        separator1.setBackground(ServerGUIConfig.BORDER_COLOR);
        separator1.setForeground(ServerGUIConfig.BORDER_COLOR);
        separator1.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, ServerGUIConfig.BORDER_COLOR));

        separator2.setBackground(ServerGUIConfig.BORDER_COLOR);
        separator2.setForeground(ServerGUIConfig.BORDER_COLOR);
        separator2.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, ServerGUIConfig.BORDER_COLOR));

        // set table
        setTableUI();

        // set server panel
        serverLogsPanel.setOpaque(true);
        serverLogsPanel.setBackground(ServerGUIConfig.BORDER_COLOR);
    }

    private void setSpinnerUI() {
        numOfRacersSpinner.setModel(new SpinnerNumberModel(6, ServerGUIConfig.MIN_NUM_OF_RACER, ServerGUIConfig.MAX_NUM_OF_RACER, 1));
        raceLengthSpinner.setModel(new SpinnerNumberModel(15, ServerGUIConfig.MIN_RACE_LENGTH, ServerGUIConfig.MAX_RACE_LENGTH, 1));

        JFormattedTextField numOfRacersTextField = ((JSpinner.DefaultEditor)numOfRacersSpinner.getEditor()).getTextField();
        numOfRacersTextField.setEditable(false);
        numOfRacersTextField.setHorizontalAlignment(SwingConstants.CENTER);

        JFormattedTextField raceLengthTextField = ((JSpinner.DefaultEditor)raceLengthSpinner.getEditor()).getTextField();
        raceLengthTextField.setEditable(false);
        raceLengthTextField.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setTableUI() {
        // set scroll pane
        statTableScrollPane.setViewportBorder(null);
        statTableScrollPane.getVerticalScrollBar().setBorder(null);
        statTableScrollPane.getHorizontalScrollBar().setBorder(null);
        statTableScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

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

        // set column width
        racerStatTable.getColumnModel().getColumn(0).setMaxWidth(50);
        racerStatTable.getColumnModel().getColumn(1).setMaxWidth(110);
        racerStatTable.getColumnModel().getColumn(2).setMaxWidth(50);
        racerStatTable.getColumnModel().getColumn(3).setMaxWidth(90);
        racerStatTable.getColumnModel().getColumn(4).setMaxWidth(70);

        // align center text in each cell
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        racerStatTable.getColumnModel().getColumn(0).setCellRenderer(center);
        racerStatTable.getColumnModel().getColumn(1).setCellRenderer(center);
        racerStatTable.getColumnModel().getColumn(2).setCellRenderer(center);
        racerStatTable.getColumnModel().getColumn(3).setCellRenderer(center);
        racerStatTable.getColumnModel().getColumn(4).setCellRenderer(center);

        dtm.addRow(new Object[]{1,"HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{2, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{3, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{4, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{5, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{6, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{7, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{8, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{9, "HHHHHHHHHH", "+10", "ELIMINATED", 10});
        dtm.addRow(new Object[]{10, "HHHHHHHHHH", "+10", "ELIMINATED", 10});

        String str = strikeThroughText((String)dtm.getValueAt(0, 3));
        dtm.setValueAt(str, 0, 3);
    }

    private String strikeThroughText(String str) {
        return "<HTML><STRIKE>"+ str +"</STRIKE></HTML>";
    }
}
