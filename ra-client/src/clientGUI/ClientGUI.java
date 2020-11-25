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

    private JPanel ClientPanel;

    private JLabel nicknameLabel;
    private JTextField enterNickname;
    private JLabel passwordLabel;
    private JPasswordField enterPassword;

    private JButton joinServerButton;
    private JButton sendAnswerButton;

    private JLabel positionLabel;
    private JLabel updatePosition;
    private JLabel pointsLabel;
    private JLabel updatePoint;

    private JLabel questionLabel;
    private JLabel updateQuestion;
    private JTextField enterAnswer;

    private JLabel timerLabel;
    private JProgressBar timerBar;

    private JPanel serverResponsePanel;

    private JSeparator separator;
    private JLabel nicknameError;
    private JLabel serverResponsePanelLabel;

    private JButton c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15;

    public ClientGUI(String gameName) {
        super(gameName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(ClientPanel);
        this.setClientGUI();

        // click join server button
        joinServerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userNickname = enterNickname.getText();
                userPassword = String.valueOf(enterPassword.getPassword());

                // verify if nickname is valid
                // if not, do not send to server
                if (checkNicknameValidity(userNickname) == false) {
                    nicknameError.setText("=> Nickname is longer than 10 or not just contain [a-zA-Z0-9_].".toUpperCase());
                    nicknameError.setHorizontalAlignment(SwingConstants.RIGHT);
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

        //        Font font = new Font("Arial", Font.BOLD, 9);
//        Map attributes = font.getAttributes();
//        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
//        Font newFont = new Font(attributes);

        this.pack();
    }

    private void setClientGUI() {
        // set panel
        ClientPanel.setBackground(ClientGUIConfig.BACKGROUND_COLOR);

        // color palette
        setColorButton();

        // set error label
        nicknameError.setFont(new Font("Arial", Font.BOLD, 9));
        nicknameError.setForeground(Color.RED);
        nicknameError.setText("".toUpperCase());

        // set label
        nicknameLabel.setForeground(ClientGUIConfig.ACCENT_COLOR);
        passwordLabel.setForeground(ClientGUIConfig.ACCENT_COLOR);
        pointsLabel.setForeground(ClientGUIConfig.ACCENT_COLOR);
        positionLabel.setForeground(ClientGUIConfig.ACCENT_COLOR);
        timerLabel.setForeground(ClientGUIConfig.ACCENT_COLOR);
        questionLabel.setForeground(ClientGUIConfig.ACCENT_COLOR);
        serverResponsePanelLabel.setForeground(ClientGUIConfig.ACCENT_COLOR);

        // set buttons
        joinServerButton.setBackground(ClientGUIConfig.ACCENT_COLOR);
        joinServerButton.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
        joinServerButton.setBorder(new LineBorder(ClientGUIConfig.ACCENT_COLOR));

        sendAnswerButton.setBackground(ClientGUIConfig.ACCENT_COLOR);
        sendAnswerButton.setForeground(ClientGUIConfig.BACKGROUND_COLOR);
        sendAnswerButton.setBorder(new LineBorder(ClientGUIConfig.ACCENT_COLOR));

        // set separator
        separator.setBackground(ClientGUIConfig.BORDER_COLOR);
        separator.setForeground(ClientGUIConfig.BORDER_COLOR);
        separator.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, ClientGUIConfig.BORDER_COLOR));

        // set text boxes
        enterNickname.setBorder(ClientGUIConfig.BORDER);
        enterPassword.setBorder(ClientGUIConfig.BORDER);
        enterAnswer.setBorder(ClientGUIConfig.BORDER);

        setEventWithTextBox();

        // set timer
        createCountDownTimer();

        // set server panel
        serverResponsePanel.setOpaque(true);
        serverResponsePanel.setBackground(ClientGUIConfig.BORDER_COLOR);
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
        timerBar.setStringPainted(true);

        timerBar.setBorder(new LineBorder(ClientGUIConfig.BORDER_COLOR, 2));
        timerBar.setForeground(ClientGUIConfig.ACCENT_COLOR);
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

    private void setColorButton() {
        List<JButton> colorButtons = Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15);

        for (int i = 0; i < NUMBER_OF_BUTTONS; ++i) {
            colorButtons.get(i).setMaximumSize(new Dimension(COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE));
            colorButtons.get(i).setPreferredSize(new Dimension(COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE));
            colorButtons.get(i).setMargin(new Insets(COLOR_BUTTON_MARGIN_TB, COLOR_BUTTON_MARGIN_LR, COLOR_BUTTON_MARGIN_TB, COLOR_BUTTON_MARGIN_LR));
            colorButtons.get(i).setHorizontalAlignment(SwingConstants.CENTER);

            colorButtons.get(i).setBackground(ClientGUIConfig.COLOR_LIST.get(i));
            colorButtons.get(i).setForeground(ClientGUIConfig.COLOR_LIST.get(i));
            colorButtons.get(i).setBorder(new LineBorder(ClientGUIConfig.COLOR_LIST.get(i)));
        }
    }

    private static boolean checkNicknameValidity(String nickname) {
        return nickname.matches("^[a-zA-Z0-9_]+$") && nickname.length() <= 10;
    }

}
