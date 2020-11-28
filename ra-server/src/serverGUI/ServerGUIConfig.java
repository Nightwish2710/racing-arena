package serverGUI;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;

public class ServerGUIConfig {
    // server game name
    public static final String GAME_NAME = "RACING ARENA: Server";

    // table configurations
    public static final int ROW_HEIGHT = 25;
    public static final String[] TABLE_COLS = {"ORDER", "RACER NAME", "GAIN", "STATUS", "POSITION"};
    public static final int[] PREFERRED_WIDTH = {50, 120, 50, 100, 70};

    // overall color theme
    public static final Color BACKGROUND_COLOR = new Color(0xF5F5F5);
    public static final Color BORDER_COLOR = new Color(0xE0E0E0);
    public static final Color LIGHT_GREEN = new Color(0x9CCC65);

    // racer answer status
    public static final Color ELIMINATED_COLOR = new Color(0xEF5350);
    public static final Color WARNING_COLOR = new Color(0xFFA726);
    public static final Color CORRECT_COLOR = new Color(0x66BB6A);
}
