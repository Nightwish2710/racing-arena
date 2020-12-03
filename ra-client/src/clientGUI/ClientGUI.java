package clientGUI;

import clientdatamodel.send.CSenLogin;
import clientobject.ClientGameConfig;
import clientobject.ClientGameMaster;

import clientnetwork.ClientNetwork;
import clientnetwork.ClientNetworkConfig;
import clientobject.ClientPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static clientGUI.ClientGUIConfig.ColorButtonConfig.*;

public class ClientGUI extends JFrame {
    private static String userNickname, userPassword;

    private int colorIndex = 0;

    private JPanel ClientPanel;

    private JLabel nicknameLabel;
    private JTextField enterNickname;
    private JLabel passwordLabel;
    private JPasswordField enterPassword;

    private JLabel victoryLabel;
    private JLabel numOfVictory;

    private JButton joinServerButton;
    private JLabel joinServerNoti;
    private JButton submitAnswerButton;

    private JLabel questionLabel;
    private JLabel firstNum, operator, secondNum, equalSign, correctAnswer;
    private JTextField enterAnswer;

    private JLabel updateStatus;
    private JLabel updateExtraStatus;

    private JLabel timerLabel;
    private JProgressBar timerBar;
    private Timer timer;

    private JSeparator separator1, separator2, separator3, separator4, separator5;
    private JSeparator verticalSeparator;

    private JLabel racerStatusLabel;
    private JPanel racerStatusPanel;
    private List<Component> racerStatusList;
    private JLabel winnerLabel;
    private JLabel winner;

    private JButton c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15;

    // error pane components
    JOptionPane noOpenConnectionPane;
    JDialog noOpenConnectionDialog;
    JButton retryButton, cancelButton;
    JLabel errorMessage;

    // Singleton
    private static ClientGUI clientGUI = null;
    public static ClientGUI getInstance() {
        if (clientGUI == null) {
            clientGUI = new ClientGUI(ClientGUIConfig.GAME_NAME);
        }
        return clientGUI;
    }

    public ClientGUI(String _gameName) {
        super(_gameName);
        clientGUI = this;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setContentPane(ClientPanel);
        this.setChangeClientGUI();
        this.setPermanentClientGUI();
        this.setButtonAction();
        this.setErrorPaneUI();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.getRootPane(submitAnswerButton).setDefaultButton(submitAnswerButton);
    }

    // dont't change the function name
    private void createUIComponents() {
        racerStatusPanel = new JPanel();
    }

    // set color for objects that change color after button click
    private void setChangeClientGUI() {
        Color ACCENT_COLOR = ClientGUIConfig.COLOR_LIST.get(colorIndex);

        // set label
        nicknameLabel.setForeground(ACCENT_COLOR);
        passwordLabel.setForeground(ACCENT_COLOR);
        timerLabel.setForeground(ACCENT_COLOR);
        questionLabel.setForeground(ACCENT_COLOR);

        racerStatusLabel.setForeground(ACCENT_COLOR);
        racerStatusLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));

        correctAnswer.setFont(new Font("Arial", Font.ITALIC, 11));
        correctAnswer.setText("correct answer ");

        numOfVictory.setForeground(ACCENT_COLOR);

        // set buttons
        joinServerButton.setBackground(ACCENT_COLOR);
        joinServerButton.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
        joinServerButton.setBorder(new LineBorder(ACCENT_COLOR));
        joinServerButton.setEnabled(false);

        submitAnswerButton.setBackground(ACCENT_COLOR);
        submitAnswerButton.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
        submitAnswerButton.setBorder(new LineBorder(ACCENT_COLOR));
        submitAnswerButton.setEnabled(false);
    }

    private void setPermanentClientGUI() {
        // set icon
        try {
            ClientGUI.getInstance().setIconImage(ImageIO.read(this.getClass().getResource("assets/dog-sharpei-icon.png")));
        } catch (IOException e) {
            System.err.println("Cannot set icon for Client UI");
            e.printStackTrace();
        }

        // set panel
        this.getContentPane().setBackground(ClientGUIConfig.BACKGROUND_COLOR);

        // color palette
        setColorButtonUI();

        // set label
        victoryLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 20));
        numOfVictory.setFont(new Font("Britannic Bold", Font.PLAIN, 40));
        joinServerNoti.setFont(new Font("Arial", Font.ITALIC, 9));

        winnerLabel.setFont(new Font("Britannic Bold", Font.PLAIN, 15));
        winner.setFont(new Font("Arial", Font.ITALIC, 9));
        winner.setText("Unknown.");

        // set separator
        setSeparatorUI();

        // set text boxes
        setTextBoxUI();
        setEventWithTextBox();

        // set answer status
        updateStatus.setFont(new Font("Arial", Font.BOLD, 9));
        updateExtraStatus.setFont(new Font("Arial", Font.ITALIC, 9));

        // create racer status bar
        setRacerStatusPanelUI();

        // set timer
        createCountDownTimer();
    }

    private void setSeparatorUI() {
        List<JSeparator> sep = Arrays.asList(separator1, separator2, separator3, separator4, separator5);

        for (int i = 0; i < sep.size(); ++i) {
            sep.get(i).setBackground(ClientGUIConfig.BORDER_COLOR);
            sep.get(i).setForeground(ClientGUIConfig.BORDER_COLOR);
            sep.get(i).setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, ClientGUIConfig.BORDER_COLOR));
        }

        verticalSeparator.setOrientation(SwingConstants.VERTICAL);
        verticalSeparator.setPreferredSize(new Dimension(3, 74));
        verticalSeparator.setBackground(ClientGUIConfig.BORDER_COLOR);
        verticalSeparator.setForeground(ClientGUIConfig.BORDER_COLOR);
        verticalSeparator.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, ClientGUIConfig.BORDER_COLOR));
    }

    private boolean checkNicknameValidity(String nickname) {
        return nickname.matches("^[a-zA-Z0-9_]+$");
    }

    private boolean isNicknameAndPasswordValid() {
        // if nickname is not empty, invalid, and "Enter your nickname" string and
        // if password is not empty, and "Enter your password" string
        // then return true else return false
        String password = String.valueOf(enterPassword.getPassword());
        if (checkNicknameValidity(enterNickname.getText()) &&
                !enterNickname.equals("Enter your nickname") && !(enterNickname.getText().length() == 0) &&
                !password.equals("Enter your password") && !(password.length() == 0)) {
            return true;
        }
        return false;
    }

    private void setTextBoxUI() {
        UIManager.put("ToolTip.background", Color.YELLOW);
        UIManager.put("ToolTip.foreground", Color.BLACK);
        UIManager.put("ToolTip.font", new Font("Calibri", Font.PLAIN, 10));

        enterNickname.setBorder(ClientGUIConfig.BORDER);
        enterNickname.setToolTipText("CASE-SENSITIVE, LENGTH <= 10, and ONLY CONTAINS [a-zA-Z0-9_]   ");
        enterNickname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { // if nickname exceeds MAX_NICKNAME_LENGTH then prevent racer to type more
                if (enterNickname.getText().length() >= ClientGUIConfig.MAX_NICKNAME_LENGTH) {
                    e.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) { // check nickname validity
                if (checkNicknameValidity(enterNickname.getText())) { // nickname is valid
                    enterNickname.setForeground((Color.BLACK));
                }
                else {
                    enterNickname.setForeground((Color.RED));
                    joinServerButton.setEnabled(false);
                }

                if (isNicknameAndPasswordValid()) { // if both nickname and password valid
                    joinServerButton.setEnabled(true);
                }
                else {
                    joinServerButton.setEnabled(false);
                }
            }
        });

        enterPassword.setBorder(ClientGUIConfig.BORDER);
        enterPassword.setToolTipText("CASE-SENSITIVE and LENGTH <= 16  ");
        enterPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { // if password exceeds MAX_PASSWORD_LENGTH then prevent racer to type more
                String password = String.valueOf(enterPassword.getPassword());
                if (password.length() >= ClientGUIConfig.MAX_PASSWORD_LENGTH) {
                    e.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (isNicknameAndPasswordValid()) { // if both nickname and password valid
                    joinServerButton.setEnabled(true);
                }
                else {
                    joinServerButton.setEnabled(false);
                }
            }
        });

        enterAnswer.setBorder(ClientGUIConfig.BORDER);
        enterAnswer.setToolTipText("Only accept INTEGER   ");
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
                    enterNickname.setForeground((Color.BLACK));
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
        timerBar.setStringPainted(true);

        timerBar.setBorder(new LineBorder(ClientGUIConfig.BORDER_COLOR, 2));
        timerBar.setForeground(Color.BLACK);
        timerBar.setBackground(ClientGUIConfig.BACKGROUND_COLOR);
        timerBar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() { return Color.BLACK; }
            protected Color getSelectionForeground() { return ClientGUIConfig.BACKGROUND_COLOR; }
        });

        timerBar.setMaximum(ClientGUIConfig.TIMER_MAX);
        timerBar.setValue(ClientGameConfig.MAX_TIMER_SEC);

        timerBar.setString(Integer.toString(ClientGameConfig.MAX_TIMER_SEC));
    }

    private void setColorButtonUI() {
        List<JButton> colorButtons = Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15);

        for (int i = 0; i < NUMBER_OF_BUTTONS; ++i) {
            colorButtons.get(i).setMaximumSize(new Dimension(COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE));
            colorButtons.get(i).setPreferredSize(new Dimension(COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE));
            colorButtons.get(i).setHorizontalAlignment(SwingConstants.CENTER);

            colorButtons.get(i).setBackground(ClientGUIConfig.COLOR_LIST.get(i));
            colorButtons.get(i).setForeground(ClientGUIConfig.COLOR_LIST.get(i));
            colorButtons.get(i).setBorder(new LineBorder(ClientGUIConfig.COLOR_LIST.get(i)));

            // assign event to each button, i.e., change color theme when clicked
            int index = i;
            colorButtons.get(i).addActionListener(e -> {
                colorIndex = index;
                setChangeClientGUI();
                changeRacerStatusBarTheme();
            });
        }
    }

    private void setButtonAction() {
        // click join server button
        joinServerButton.addActionListener(e -> {
            userNickname = enterNickname.getText();
            userPassword = String.valueOf(enterPassword.getPassword());

            ClientGameMaster.getInstance().getCRacer().setNickname(userNickname);
            ClientGameMaster.getInstance().getCRacer().setPassword(userPassword);

            CSenLogin cdLogin = new CSenLogin(ClientNetworkConfig.CMD.CMD_LOGIN, userNickname, userPassword);
            ClientNetwork.getInstance().send(cdLogin);

            submitAnswerButton.setEnabled(false); // not allow racers to resubmit their answer
        });

        // click to submit answer
        submitAnswerButton.addActionListener(e -> {
            submitAnswerButton.setEnabled(false);
            submitAnswer();
        });
        // press [Enter] to submit answer
        submitAnswerButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER && submitAnswerButton.isEnabled()) {
                    submitAnswerButton.setEnabled(false);
                    submitAnswer();
                }
            }
        });
    }

    private CompoundBorder createProgressBarBorder(int rightThickness) {
        Border line = BorderFactory.createMatteBorder(0, 0, 0, rightThickness, ClientGUIConfig.BORDER_COLOR);
        Border empty = new EmptyBorder(2, 2, 2, 2);
        CompoundBorder border = new CompoundBorder(line, empty);

        return border;
    }

    // create progress bar for every race with the same template
    private JProgressBar createRacerStatusBar(int rightThickness) {
        JProgressBar tmpBar = new JProgressBar();

        tmpBar.setStringPainted(false);
        tmpBar.setVisible(true);

        tmpBar.setMinimumSize(new Dimension(180, 20));
        tmpBar.setBorder(createProgressBarBorder(rightThickness));
        tmpBar.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
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
        tmpBar.setValue(ClientGUIConfig.INIT_POSITION);
        tmpBar.setString(Integer.toString(ClientGUIConfig.INIT_POSITION));

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
        Color ACCENT_COLOR = ClientGUIConfig.COLOR_LIST.get(colorIndex);

        gbconstraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel tmpLabel = new JLabel();
        tmpLabel.setMinimumSize(new Dimension(ClientGUIConfig.RACER_STAT_PANEL_LABEL_WIDTH, 20));
        tmpLabel.setText("<HTML>&#x2666; Me &#x2666;</HTML>");
        tmpLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        tmpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        tmpLabel.setVerticalAlignment(SwingConstants.CENTER);
        addComponent(tmpLabel, racerStatusPanel, gblayout, gbconstraints, 0, 0); // label on the left

        JProgressBar tmpBar = createRacerStatusBar(3);
        tmpBar.setStringPainted(true);
        tmpBar.setForeground(ACCENT_COLOR);
        addComponent(tmpBar, racerStatusPanel, gblayout, gbconstraints, 1, 0); // progress bar on the right
    }

    // create a line to separate between current racer and other racers
    private void createSeparatorBetweenYouAndOtherRacers(GridBagLayout gblayout, GridBagConstraints gbconstraints) {
        JSeparator separator4 = new JSeparator();
        separator4.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, ClientGUIConfig.BACKGROUND_COLOR));
        separator4.setAlignmentY(Component.TOP_ALIGNMENT);

        gbconstraints.gridwidth = 2; // separator will go across 2 cells
        gbconstraints.ipadx = ClientGUIConfig.RACER_STAT_PANEL_WIDTH; // padding width
        gbconstraints.ipady = 0; // padding height

        addComponent(separator4, racerStatusPanel, gblayout, gbconstraints, 0, 1);
    }

    // create a grid bag layout to dynamically add racer progress bar
    private void setRacerStatusPanelUI() {
        GridBagLayout gblayout = new GridBagLayout();
        GridBagConstraints gbconstraints = new GridBagConstraints();
        gbconstraints.fill = GridBagConstraints.HORIZONTAL;
        gbconstraints.insets = new Insets(0, 2, 0, 2);
        gbconstraints.weightx = 1;
        gbconstraints.weighty = 1;

        racerStatusPanel.setBackground(ClientGUIConfig.BACKGROUND_COLOR);
        racerStatusPanel.setPreferredSize(new Dimension(250, -1));
        racerStatusPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        racerStatusPanel.setLayout(gblayout);

        createYouProgressBar(gblayout, gbconstraints);
        createSeparatorBetweenYouAndOtherRacers(gblayout, gbconstraints);

        // reset parameter to correctly add labels and progress bars
        gbconstraints.gridwidth = 1;
        gbconstraints.ipadx = 0;
        gbconstraints.ipady = 2;

        for (int i = 0; i < ClientGameConfig.MAX_NUM_OF_RACERS - 1; ++i) {
            JLabel tmpLabel = new JLabel();
            tmpLabel.setMinimumSize(new Dimension(ClientGUIConfig.RACER_STAT_PANEL_LABEL_WIDTH, 25));
            tmpLabel.setText("ImpostorNo" + Integer.toString(i+1));
            tmpLabel.setFont(new Font("Arial", Font.PLAIN, 9));
            tmpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            tmpLabel.setVerticalAlignment(SwingConstants.CENTER);
            tmpLabel.setVisible(false);
            addComponent(tmpLabel, racerStatusPanel, gblayout, gbconstraints, 0, i+2); // label on the left

            JProgressBar tmpBar = createRacerStatusBar(0);
            addComponent(tmpBar, racerStatusPanel, gblayout, gbconstraints, 1, i+2); // progress bar on the right
        }

        racerStatusList = Arrays.asList(racerStatusPanel.getComponents());
    }

    // change racers' progress bar theme
    private void changeRacerStatusBarTheme() {
        // change current racer's progress bar
        racerStatusList.get(1).setForeground(ClientGUIConfig.COLOR_LIST.get(colorIndex));

        // change other racers' progress bar
        for (int i = 2; i < ClientGameMaster.getInstance().getNumOfRacers() + 1; ++i) {
            racerStatusList.get(i*2).setForeground(ClientGUIConfig.COLOR_LIST.get(colorIndex));
        }
    }

    private void setErrorPaneUI() {
        retryButton = new JButton();

        retryButton.setText("RETRY");
        retryButton.setPreferredSize(new Dimension(80, 25));
        retryButton.setBackground(ClientGUIConfig.COLOR_LIST.get(9));
        retryButton.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
        retryButton.setBorder(new LineBorder(ClientGUIConfig.COLOR_LIST.get(9)));

        retryButton.addActionListener(e -> { ClientNetwork.getInstance().connect(); });

        cancelButton = new JButton();

        cancelButton.setText("CANCEL");
        cancelButton.setPreferredSize(new Dimension(80, 25));
        cancelButton.setBackground(ClientGUIConfig.COLOR_LIST.get(0));
        cancelButton.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
        cancelButton.setBorder(new LineBorder(ClientGUIConfig.COLOR_LIST.get(0)));

        cancelButton.addActionListener(e -> { System.exit(-1); });

        errorMessage = new JLabel("<HTML><center>NO OPEN CONNECTION FOR CLIENT</center><HTML>");
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setFont(new Font("Arial", Font.BOLD, 13));

        noOpenConnectionDialog = new JDialog((Frame)null, Dialog.ModalityType.TOOLKIT_MODAL);
        noOpenConnectionDialog.setTitle("CONNECTION ERROR");

        noOpenConnectionPane = new JOptionPane(errorMessage, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION);
        noOpenConnectionPane.setOptions(new Object[]{retryButton, cancelButton});

        try {
            noOpenConnectionDialog.setIconImage(ImageIO.read(this.getClass().getResource("assets/metal-error.png")));
        } catch (IOException e) {
            System.err.println("Cannot set icon for Error Message Popup");
            e.printStackTrace();
        }

        noOpenConnectionDialog.setContentPane(noOpenConnectionPane);
        noOpenConnectionDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        noOpenConnectionDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(-1);
            }
        });

        noOpenConnectionDialog.pack();
        noOpenConnectionDialog.setLocationRelativeTo(null);
        noOpenConnectionDialog.setVisible(false);
    }

    public void setJoinServerNoti(String str, int color) {
        joinServerNoti.setForeground(ClientGUIConfig.COLOR_LIST.get(color));
        joinServerNoti.setText(str);
    }

    public void disableComponentAfterJoinServer() {
        enterNickname.setEnabled(false);
        enterPassword.setEnabled(false);
        joinServerButton.setEnabled(false);
    }

    public void turnOnNoOpenConnectionPane() {
        noOpenConnectionDialog.setVisible(true);
    }

    public void turnOffNoOpenConnectionPane() {
        if (this.noOpenConnectionDialog == null) return;
        this.noOpenConnectionDialog.setVisible(false);
    }

    public void updateYouNumOfVictory(int numOfVictory) {
        this.numOfVictory.setText(Integer.toString(numOfVictory));
    }

    public void updateRacersProgressBarSize(int raceLength) {
        ((JProgressBar)racerStatusList.get(1)).setMaximum(raceLength);

        for (int i = 2; i < ClientGameMaster.getInstance().getNumOfRacers() + 1; ++i) {
            ((JProgressBar)racerStatusList.get(i*2)).setMaximum(raceLength);
        }
    }

    public void updateYouNickname(String nickname) {
        ((JLabel)racerStatusList.get(0)).setText(nickname);
    }

    public void startAnswering() throws InterruptedException {
        if (ClientGameMaster.getInstance().getCRacer().getStatusFlag() != ClientGameConfig.RACER_STATUS_FLAG.FLAG_ELIMINATED) {
            enterAnswer.setEnabled(true);
            submitAnswerButton.setEnabled(true);
        }

        correctAnswer.setFont(new Font("Arial", Font.ITALIC, 11));
        correctAnswer.setText("correct answer ");

        CountDownLatch lock = new CountDownLatch(ClientGameConfig.MAX_TIMER_SEC);

        timer = new Timer(1000, new ActionListener() {
            int counter = ClientGameConfig.MAX_TIMER_SEC;

            public void actionPerformed(ActionEvent ae) {
                --counter;
                timerBar.setValue(counter);
                timerBar.setString(Integer.toString(counter));
                lock.countDown();
                if (counter < 1) {
                    timer.stop();
                }
            }
        });

        timer.start();

        try {
            lock.await();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public void stopAnswering() {
        System.out.println("STOP ANSWERING");

        timerBar.setValue(ClientGameConfig.MAX_TIMER_SEC);
        timerBar.setString(Integer.toString(ClientGameConfig.MAX_TIMER_SEC));

        enterAnswer.setEnabled(false);
        submitAnswerButton.setEnabled(false);
    }

    public void setFirstNum(int firstNum) { this.firstNum.setText(Integer.toString(firstNum)); }
    public void setOperator(int operator) { this.operator.setText(ClientGameConfig.OPERATORS[operator]); }
    public void setSecondNum(int secondNum) { this.secondNum.setText(Integer.toString(secondNum)); }

    private void submitAnswer() {
        int answer;
        try {
            answer = Integer.parseInt(this.enterAnswer.getText());
        } catch (NumberFormatException e) {
            answer = Integer.MAX_VALUE;
        }
        ClientGameMaster.getInstance().giveAnswer(answer);
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

    public void updateYouPoint(int point) {
        ((JProgressBar)racerStatusList.get(1)).setValue(point);
        ((JProgressBar)racerStatusList.get(1)).setString(String.valueOf(point));
    }

    public void resetYouProgressBar() {
        ((JProgressBar)racerStatusList.get(1)).setValue(ClientGameConfig.INIT_RACER_POSITION); // reset progress
        ((JProgressBar)racerStatusList.get(1)).setString(Integer.toString(ClientGameConfig.INIT_RACER_POSITION)); // reset progress number
    }

    public void strikeThroughYouNickname() {
        String str = ((JLabel)racerStatusList.get(0)).getText();
        ((JLabel)racerStatusList.get(0)).setText(strikeThroughText(str));
    }

    public void setUpdateStatus(String status) { updateStatus.setText(status); }
    public void setUpdateExtraStatus(String status) { updateExtraStatus.setText(status); }

    public void initOpponentProgressWhenReceiveNumOfPplJoinning() {
        ((JSeparator)racerStatusList.get(2)).setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, ClientGUIConfig.BORDER_COLOR));

        for (int i = 2; i < ClientGameMaster.getInstance().getNumOfRacers() + 1; ++i) {
            racerStatusList.get(i*2-1).setVisible(true); // show opponent name

            JProgressBar tmpBar = (JProgressBar)(racerStatusList.get(i*2));
            tmpBar.setForeground(ClientGUIConfig.COLOR_LIST.get(colorIndex)); // show opponent bar
            tmpBar.setStringPainted(true); // show opponent bar value
            tmpBar.setBorder(createProgressBarBorder(3)); // show finnish line
        }
    }

    // update the progress bar to show how far each racer has come
    // first label to contain "ImpostorNo" will be the empty slot
    public void updateOpponentNameWhenJoin(ClientPlayer opponent) {
        for (int i = 2; i < ClientGameMaster.getInstance().getNumOfRacers() + 1; ++i) {
            if (((JLabel)racerStatusList.get(i*2-1)).getText().equals("ImpostorNo"+(i-1))) {
                ((JLabel)racerStatusList.get(i*2-1)).setText(opponent.getNickname()); // update opponent name

                break;
            }
        }
    }

    public void updateOpponentProgress(ClientPlayer opponent) {
        for (int i = 2; i < ClientGameMaster.getInstance().getNumOfRacers() + 1; ++i) {
            String str = ((JLabel)racerStatusList.get(i*2-1)).getText();

            if (checkIfEqualToNickname(str, opponent.getNickname())) {
                ((JProgressBar)racerStatusList.get(i*2)).setValue(opponent.getPosition()); // update opponent progress
                ((JProgressBar)racerStatusList.get(i*2)).setString(Integer.toString(opponent.getPosition())); // update progress number

                break;
            }
        }
    }

    // update other racers' IU when a racer quit
    public void updateOpponentProgressWhenARacerQuit(ClientPlayer opponent) {
        for (int i = 2; i < ClientGameMaster.getInstance().getNumOfRacers() + 1; ++i) {
            String str = ((JLabel)racerStatusList.get(i*2-1)).getText();

            if (checkIfEqualToNickname(str, opponent.getNickname())) {
                ((JLabel)racerStatusList.get(i*2-1)).setText("ImpostorNo"+(i-1)); // reset opponent name

                ((JProgressBar)racerStatusList.get(i*2)).setValue(ClientGameConfig.INIT_RACER_POSITION); // reset progress
                ((JProgressBar)racerStatusList.get(i*2)).setString(Integer.toString(ClientGameConfig.INIT_RACER_POSITION)); // reset progress number

                break;
            }
        }
    }

    public void updateCorrectAnswer(int answer) {
        correctAnswer.setFont(new Font("Arial", Font.PLAIN, 12));
        correctAnswer.setText(Integer.toString(answer));
    }

    public void strikeThroughEliminatedRacer(ClientPlayer opponent) {
        for (int i = 2; i < ClientGameMaster.getInstance().getNumOfRacers() + 1; ++i) {
            String str = ((JLabel)racerStatusList.get(i*2-1)).getText();

            if (checkIfEqualToNickname(str, opponent.getNickname())) {
                String nickName = ((JLabel)racerStatusList.get(i*2-1)).getText();
                ((JLabel)racerStatusList.get(i*2-1)).setText(strikeThroughText(nickName)); // strike through opponent name

                break;
            }
        }
    }

    public void resetUIForReplay() {
        // reset winner announcement
        winner.setFont(new Font("Arial", Font.ITALIC, 9));
        winner.setText("Unknown.");

        // reset question info
        firstNum.setText("1st no.");
        operator.setText("op");
        secondNum.setText("2nd no.");

        correctAnswer.setFont(new Font("Arial", Font.ITALIC, 11));
        correctAnswer.setText("correct answer ");

        // reset answer status
        updateStatus.setText("Answer status");
        updateStatus.setText("Extra status");
    }

    public void updateNumOfVictory(int victory) {
        numOfVictory.setText(Integer.toString(victory));
    }

    public void announceWinner(String winnerName) {
        winner.setFont(new Font("Britannic Bold", Font.PLAIN, 18));
        winner.setText(winnerName);
    }

    public void announceNoWinner() {
        winner.setFont(new Font("Arial", Font.ITALIC, 9));
        winner.setText("Ppl in this server are not so smart :> ");
    }

    public void renewRacerNickname() {
        JLabel tmpLabel = (JLabel)racerStatusList.get(0);
        tmpLabel.setText(removeStrikeThrough(tmpLabel.getText()));

        for (int i = 2; i < ClientGameMaster.getInstance().getNumOfRacers() + 1; ++i) {
            tmpLabel = (JLabel)racerStatusList.get(i*2-1);
            tmpLabel.setText(removeStrikeThrough(tmpLabel.getText()));
        }
    }
}