package clientGUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
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

    public ClientGUI(String gameName) {
        super(gameName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(ClientPanel);
        this.setClientGUI();

        this.pack();
    }

    private void setClientGUI() {
        // set panel
        ClientPanel.setBackground(ClientGUIConfig.LIGHT_ORANGE);

        // set buttons
        joinServerButton.setBackground(ClientGUIConfig.DARK_ORANGE);
        joinServerButton.setBorder(new LineBorder(ClientGUIConfig.DARK_ORANGE));

        sendAnswerButton.setBackground(ClientGUIConfig.DARK_ORANGE);
        sendAnswerButton.setBorder(new LineBorder(ClientGUIConfig.DARK_ORANGE));

        // set separator
        separator.setBackground(ClientGUIConfig.DARK_ORANGE);
        separator.setForeground(ClientGUIConfig.DARK_ORANGE);

        // set text boxes
        enterNickname.setBorder(ClientGUIConfig.BORDER);
        enterPassword.setBorder(ClientGUIConfig.BORDER);
        enterAnswer.setBorder(ClientGUIConfig.BORDER);

        setEventWithTextBox();

        // set timer
        createCountDownTimer();

        // set server panel
        serverResponsePanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(ClientGUIConfig.DARK_ORANGE, 2), " SERVER RESPONSES   "
                )
        );
        serverResponsePanel.setOpaque(false);
    }

    private void setEventWithTextBox() {
        enterNickname.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { // if cursor is in the box
                enterNickname.setText(null);
            }
            @Override
            public void focusLost(FocusEvent e) { // if cursor is not in the box
                enterNickname.setText("Enter your nickname");
            }
        });

        enterPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { // if cursor is in the box
                enterPassword.setText(null);
            }
            @Override
            public void focusLost(FocusEvent e) { // if cursor is not in the box
                enterPassword.setText("Enter your password");
            }
        });

        enterAnswer.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { // if cursor is in the box
                enterAnswer.setText(null);
            }
            @Override
            public void focusLost(FocusEvent e) { // if cursor is not in the box
                enterAnswer.setText("Enter your answer");
            }
        });
    }

    private void createCountDownTimer() {
        timerBar.setStringPainted(true);
        timerBar.setBorderPainted(false);
        timerBar.setBorder(new LineBorder(ClientGUIConfig.DARK_ORANGE));
        timerBar.setForeground(ClientGUIConfig.DARK_ORANGE);
        timerBar.setBackground(ClientGUIConfig.LIGHT_ORANGE);

        timerBar.setMaximum(ClientGUIConfig.TIMER_MAX);
        timerBar.setValue(1);

        timerBar.setString(Integer.toString(1));
    }

}
