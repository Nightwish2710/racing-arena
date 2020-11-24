package clientGUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ClientGUIConfig {
    public static final String GAME_NAME = "RACING ARENA";
    public static final Color DARK_ORANGE = new Color(0xD79B00);
    public static final Color LIGHT_ORANGE = new Color(0xFFE6CC);

    public static final int TIMER_MAX = 10;

    private static Border line = BorderFactory.createLineBorder(ClientGUIConfig.DARK_ORANGE, 1);
    private static Border empty = new EmptyBorder(5, 5, 5, 5);
    public static final CompoundBorder BORDER = new CompoundBorder(line, empty);
}
