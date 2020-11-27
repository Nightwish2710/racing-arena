package clientGUI;

import clientdatamodel.CDAccount;
import clientnetwork.ClientNetwork;
import clientnetwork.ClientNetworkConfig;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static clientGUI.ClientGUIConfig.ColorButtonConfig.*;

public class ClientGUI extends JFrame {
    public static String userNickname, userPassword;

    private int colorIndex = 0;
    private boolean create = true, changeTheme = false, updatePoint = false;

    private JPanel ClientPanel;

    private JLabel nicknameLabel;
    private JTextField enterNickname;
    private JLabel passwordLabel;
    private JPasswordField enterPassword;

    private JLabel victoryLabel;
    private JLabel updateNumOfVictory;

    private JButton joinServerButton;
    private JLabel joinServerNoti;
    private JButton sendAnswerButton;

    private JLabel questionLabel;
    private JLabel firstNum, operator, secondNum;
    private JTextField enterAnswer;

    private JLabel updateStatus;
    private JLabel updateExtraStatus;

    private JLabel timerLabel;
    private JProgressBar timerBar;

    private JScrollPane serverResponsePane;

    private JSeparator separator1, separator2, separator3;
    private List<JSeparator> sep = Arrays.asList(separator1, separator2, separator3);

    private JSeparator verticalSeparator;

    private JLabel serverResponsePanelLabel;

    private JLabel racerStatusLabel;
    private JPanel racerStatusPanel;

    private JButton c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15;
    private List<JButton> colorButtons = Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15);

    public ClientGUI(String gameName) {
        super(gameName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(ClientPanel);
        this.setChangeClientGUI();
        this.setPermanentClientGUI();
        this.setButtonAction();

        this.pack();
    }

    // set color for objects that change color after button click
    private void setChangeClientGUI() {
        Color ACCENT_COLOR = ClientGUIConfig.COLOR_LIST.get(colorIndex);

        // set label
        nicknameLabel.setForeground(ACCENT_COLOR);
        passwordLabel.setForeground(ACCENT_COLOR);
        timerLabel.setForeground(ACCENT_COLOR);
        questionLabel.setForeground(ACCENT_COLOR);

        serverResponsePanelLabel.setForeground(ACCENT_COLOR);
        serverResponsePanelLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));

        racerStatusLabel.setForeground(ACCENT_COLOR);
        racerStatusLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));

        updateNumOfVictory.setForeground(ACCENT_COLOR);

        // set buttons
        joinServerButton.setBackground(ACCENT_COLOR);
        joinServerButton.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
        joinServerButton.setBorder(new LineBorder(ACCENT_COLOR));

        sendAnswerButton.setBackground(ACCENT_COLOR);
        sendAnswerButton.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
        sendAnswerButton.setBorder(new LineBorder(ACCENT_COLOR));

        // set timer
        createCountDownTimer();
    }

    private void setPermanentClientGUI() {
        // set panel
        ClientPanel.setBackground(ClientGUIConfig.BACKGROUND_COLOR);

        // color palette
        setColorButtonUI();

        // set label
        victoryLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));
        updateNumOfVictory.setFont(new Font("Britannic Bold", Font.PLAIN, 35));
        joinServerNoti.setFont(new Font("Arial", Font.ITALIC, 9));

        // set separator
        setSeparatorUI();

        // set text boxes
        enterNickname.setBorder(ClientGUIConfig.BORDER);
        enterNickname.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                enterNickname.setToolTipText("Nickname cannot be longer than 10 and only contains [a-zA-Z0-9_].");
                enterNickname.setForeground(Color.BLACK);
            }
            @Override
            public void focusLost(FocusEvent e) {
                enterNickname.setToolTipText("Nickname cannot be longer than 10 and only contains [a-zA-Z0-9_].");
            }
        });
        enterPassword.setBorder(ClientGUIConfig.BORDER);
        enterAnswer.setBorder(ClientGUIConfig.BORDER);

        updateStatus.setFont(new Font("Arial", Font.BOLD, 9));
        updateExtraStatus.setFont(new Font("Arial", Font.BOLD, 9));

        setEventWithTextBox();

        // set server response scroll pane
        setServerResponsePaneUI();

        createUIComponents();
        createRacerStatusBar();
    }

    private void setSeparatorUI() {
        for (int i = 0; i < sep.size(); ++i) {
            sep.get(i).setBackground(ClientGUIConfig.BORDER_COLOR);
            sep.get(i).setForeground(ClientGUIConfig.BORDER_COLOR);
            sep.get(i).setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, ClientGUIConfig.BORDER_COLOR));
        }

        verticalSeparator.setOrientation(SwingConstants.VERTICAL);
        verticalSeparator.setPreferredSize(new Dimension(3, 67));
        verticalSeparator.setBackground(ClientGUIConfig.BORDER_COLOR);
        verticalSeparator.setForeground(ClientGUIConfig.BORDER_COLOR);
        verticalSeparator.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, ClientGUIConfig.BORDER_COLOR));
    }

    private void setEventWithTextBox() {
        enterNickname.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { // if cursor is in the box
                if (enterNickname.getText().equals("Enter your nickname")) {
                    enterNickname.setText(null);
                }
            }
            @Override
            public void focusLost(FocusEvent e) { // if cursor is not in the box
                if (enterNickname.getText().equals("")) {
                    enterNickname.setText("Enter your nickname");
                }
            }
        });

        enterPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { // if cursor is in the box
                if (String.valueOf(enterPassword.getPassword()).equals("Enter your password")) {
                    enterPassword.setText(null);
                }
            }
            @Override
            public void focusLost(FocusEvent e) { // if cursor is not in the box
                if (String.valueOf(enterPassword.getPassword()).equals("")) {
                    enterPassword.setText("Enter your password");
                }
            }
        });

        enterAnswer.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { // if cursor is in the box
                if (enterAnswer.getText().equals("Enter your answer")) {
                    enterAnswer.setText(null);
                }
            }
            @Override
            public void focusLost(FocusEvent e) { // if cursor is not in the box
                if (enterAnswer.getText().equals("")) {
                    enterAnswer.setText("Enter your answer");}
            }
        });
    }

    private void createCountDownTimer() {
        Color ACCENT_COLOR = ClientGUIConfig.COLOR_LIST.get(colorIndex);

        timerBar.setStringPainted(true);

        timerBar.setBorder(new LineBorder(ClientGUIConfig.BORDER_COLOR, 2));
        timerBar.setForeground(ACCENT_COLOR);
        timerBar.setBackground(ClientGUIConfig.BACKGROUND_COLOR);
        timerBar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.BLACK;
            }
            protected Color getSelectionForeground() {
                return ClientGUIConfig.BACKGROUND_COLOR;
            }
        });

        timerBar.setMaximum(ClientGUIConfig.TIMER_MAX);
        timerBar.setValue(1);

        timerBar.setString(Integer.toString(1));
    }

    private void setColorButtonUI() {
        for (int i = 0; i < NUMBER_OF_BUTTONS; ++i) {
            colorButtons.get(i).setMaximumSize(new Dimension(COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE));
            colorButtons.get(i).setPreferredSize(new Dimension(COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE));
//            colorButtons.get(i).setMargin(new Insets(COLOR_BUTTON_MARGIN_TB, COLOR_BUTTON_MARGIN_LR, COLOR_BUTTON_MARGIN_TB, COLOR_BUTTON_MARGIN_LR));
            colorButtons.get(i).setHorizontalAlignment(SwingConstants.CENTER);

            colorButtons.get(i).setBackground(ClientGUIConfig.COLOR_LIST.get(i));
            colorButtons.get(i).setForeground(ClientGUIConfig.COLOR_LIST.get(i));
            colorButtons.get(i).setBorder(new LineBorder(ClientGUIConfig.COLOR_LIST.get(i)));

            // assign event to each button, i.e., change color theme when clicked
            int index = i;
            colorButtons.get(i).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    colorIndex = index;
                    changeTheme = true;
                    setChangeClientGUI();
                    createUIComponents();
                }
            });
        }
    }

    private void setServerResponsePaneUI() {
        serverResponsePane.setBorder(BorderFactory.createLineBorder(ClientGUIConfig.BORDER_COLOR, 5));
        serverResponsePane.getViewport().setBackground(ClientGUIConfig.BORDER_COLOR);
        serverResponsePane.setOpaque(true);

        serverResponsePane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(ClientGUIConfig.BACKGROUND_COLOR);
                button.setBorder(BorderFactory.createLineBorder(ClientGUIConfig.BACKGROUND_COLOR, 1));
                return button;
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(ClientGUIConfig.BACKGROUND_COLOR);
                button.setBorder(BorderFactory.createLineBorder(ClientGUIConfig.BACKGROUND_COLOR, 1));
                return button;
            }
        });
    }

    private void setButtonAction() {
        // click join server button
        joinServerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userNickname = enterNickname.getText();
                userPassword = String.valueOf(enterPassword.getPassword());

                // verify if nickname is valid
                // if not, do not send to server
                if (checkNicknameValidity(userNickname) == false) {
                    enterNickname.setForeground(Color.RED);
                }
                else {
                    CDAccount cdLogin = new CDAccount(userNickname, userPassword);
                    ClientNetwork.getInstance().send(ClientNetworkConfig.CMD.CMD_LOGIN, cdLogin);
                }
            }
        });

        // click send answer button
        sendAnswerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    // create progress bar for every race with the same template
    private JProgressBar createRacerStatusBar() {
        Color ACCENT_COLOR = ClientGUIConfig.COLOR_LIST.get(colorIndex);

        Border line = BorderFactory.createMatteBorder(0, 0, 0, 3, ClientGUIConfig.BORDER_COLOR);
        Border empty = new EmptyBorder(2, 2, 2, 2);
        CompoundBorder border = new CompoundBorder(line, empty);

        JProgressBar tmpBar = new JProgressBar();

        tmpBar.setStringPainted(true);
        tmpBar.setBorder(border);
        tmpBar.setForeground(ACCENT_COLOR);
        tmpBar.setBackground(ClientGUIConfig.BACKGROUND_COLOR);
        tmpBar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() {
                return Color.BLACK;
            }
            protected Color getSelectionForeground() {
                return ClientGUIConfig.BACKGROUND_COLOR;
            }
        });

        tmpBar.setMaximum(ClientGUIConfig.MAX_RACE_LENGTH);
        tmpBar.setValue(15);
        tmpBar.setString(Integer.toString(15));

        return tmpBar;
    }

    // add component to racer status panel
    private void addComponent(Component component, Container racerStatusPanel,
                          GridBagLayout gblayout, GridBagConstraints gbconstraints,
                          int gridx, int gridy) {

        gbconstraints.gridx = gridx;
        gbconstraints.gridy = gridy;

        gblayout.setConstraints(component, gbconstraints);
        racerStatusPanel.add(component);
    }

    // create current racer progress bar first
    private void createYouProgressBar(GridBagLayout gblayout, GridBagConstraints gbconstraints) {
        gbconstraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel tmpLabel = new JLabel();
        tmpLabel.setText("YOU");
        tmpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        addComponent(tmpLabel, racerStatusPanel, gblayout, gbconstraints, 0, 0); // label on the left

        JProgressBar tmpBar = createRacerStatusBar();
        addComponent(tmpBar, racerStatusPanel, gblayout, gbconstraints, 1, 0); // progress bar on the right
    }

    // craete a line to separate between current racer and other racers
    private void createSeparatorBetweenYouAndOtherRacers(GridBagLayout gblayout, GridBagConstraints gbconstraints) {
        JSeparator separator4 = new JSeparator();
        separator4.setBackground(ClientGUIConfig.BORDER_COLOR);
        separator4.setForeground(ClientGUIConfig.BORDER_COLOR);
        separator4.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, ClientGUIConfig.BORDER_COLOR));

        gbconstraints.gridwidth = 2; // separator will go across 2 cells
        gbconstraints.ipadx = ClientGUIConfig.RACER_STAT_PANEL_WIDTH; // padding width
        gbconstraints.ipady = 2; // padding height

        addComponent(separator4, racerStatusPanel, gblayout, gbconstraints, 0, 1);
    }

    // dont't change the function name
    private void createUIComponents() {
        if (create) { // create a grid bag layout to dynamically add racer progress bar

            GridBagLayout gblayout = new GridBagLayout();
            GridBagConstraints gbconstraints = new GridBagConstraints();
            gbconstraints.fill = GridBagConstraints.HORIZONTAL;
            gbconstraints.insets = new Insets(0, 2, 0, 2);
            gbconstraints.weightx = 1;

            racerStatusPanel = new JPanel();
            racerStatusPanel.setBackground(ClientGUIConfig.BACKGROUND_COLOR);
            racerStatusPanel.setPreferredSize(new Dimension(250, -1));
            racerStatusPanel.setLayout(gblayout);

            List<String> tmpStr = Arrays.asList("H", "HH", "HHH", "HHHH", "HHHHH", "HHHHHH", "HHHHHHH", "HHHHHHHH", "HHHHHHHHH", "HHHHHHHHHH");

            createYouProgressBar(gblayout, gbconstraints);
            createSeparatorBetweenYouAndOtherRacers(gblayout, gbconstraints);

            // reset parameter to correctly add labels and progress bars
            gbconstraints.gridwidth = 1;
            gbconstraints.ipady = -1;

            for (int i = 0; i < 10; ++i) {
                // 1st column width
                gbconstraints.ipadx = ClientGUIConfig.RACER_STAT_PANEL_LABEL_WIDTH;

                JLabel tmpLabel = new JLabel();
                tmpLabel.setText(tmpStr.get(i));
                tmpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                addComponent(tmpLabel, racerStatusPanel, gblayout, gbconstraints, 0, i+2); // label on the left

                // 2nd column width
                gbconstraints.ipadx = ClientGUIConfig.RACER_STAT_PANEL_WIDTH - ClientGUIConfig.RACER_STAT_PANEL_LABEL_WIDTH;

                JProgressBar tmpBar = createRacerStatusBar();
                addComponent(tmpBar, racerStatusPanel, gblayout, gbconstraints, 1, i+2); // progress bar on the right
            }

            create = false;
        }
        else if (changeTheme) { // change racers' progress bar theme

            List<Component> racerStatusList = Arrays.asList(racerStatusPanel.getComponents());

            // change current racer's progress bar
            ((JProgressBar)racerStatusList.get(1)).setForeground(ClientGUIConfig.COLOR_LIST.get(colorIndex));

            // change other racers' progress bar
            for (int i = 2; i < (racerStatusList.size()+1) / 2; ++i) {
                ((JProgressBar)racerStatusList.get(i*2)).setForeground(ClientGUIConfig.COLOR_LIST.get(colorIndex));
            }

            changeTheme = false;
        }
        else if (updatePoint) { // update the progress bar to show how far each racer has come

            List<Component> racerStatusList = Arrays.asList(racerStatusPanel.getComponents());

            for (int i = 0; i < racerStatusList.size(); ++i) {
                ((JProgressBar)racerStatusList.get(i)).setValue(8);
                ((JProgressBar)racerStatusList.get(i)).setString(Integer.toString(8));
            }

            updatePoint = false;
        }
    }

    private static boolean checkNicknameValidity(String nickname) {
        return nickname.matches("^[a-zA-Z0-9_]+$") && nickname.length() <= 10;
    }

    private void disableComponentAfterJoinServer() {
        enterNickname.setEnabled(false);
        enterPassword.setEnabled(false);
        joinServerButton.setEnabled(false);
    }
}
