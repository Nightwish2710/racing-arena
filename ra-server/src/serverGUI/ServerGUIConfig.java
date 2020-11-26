package serverGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ServerGUIConfig {
    public static final String GAME_NAME = "RACING ARENA: Server";

    public static final int NUM_FIELD = 5;
    public static final int ROW_HEIGHT = 25;
    public static final String[] TABLE_COLS = {"ORDER", "RACER NAME", "GAIN", "STATUS", "POSITION"};

    public static final int MIN_NUM_OF_RACER = 2;
    public static final int MAX_NUM_OF_RACER = 10;

    public static final int MIN_RACE_LENGTH = 3;
    public static final int MAX_RACE_LENGTH = 26;

    public static final Color BACKGROUND_COLOR = new Color(0xF5F5F5);
    public static final Color BORDER_COLOR = new Color(0xE0E0E0);
    public static final Color LIGHT_GREEN = new Color(0x9CCC65);

    public static final Color ELIMINATED_COLOR = new Color(0xEF5350);
    public static final Color WARNING_COLOR = new Color(0xFFA726);
    public static final Color CORRECT_COLOR = new Color(0x66BB6A);

//    public static final Icon WINNING_ICON = new ImageIcon("../res/winning-cup.png");
    public static final String WINNING_ICON = "winning-cup.png";
    public static final Icon CUP = UIManager.getIcon("Cup.ico");
}
