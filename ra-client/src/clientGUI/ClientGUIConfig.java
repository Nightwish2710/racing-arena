package clientGUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ClientGUIConfig {
    // client game name
    public static final String GAME_NAME = "RACING ARENA: ClientRacer";

    // nickname and passwork max length
    public static final int MAX_NICKNAME_LENGTH = 10;
    public static final int MAX_PASSWORD_LENGTH = 16;

    // color
    public static final Color BACKGROUND_COLOR = new Color(0xF5F5F5);
    public static final Color BORDER_COLOR = new Color(0xE0E0E0);
    public static Color ACCENT_COLOR = Palette.RED;

    // button border
    private static Border line = BorderFactory.createLineBorder(ClientGUIConfig.BORDER_COLOR, 2);
    private static Border empty = new EmptyBorder(5, 5, 5, 5);
    public static final CompoundBorder BORDER = new CompoundBorder(line, empty);

    // timer configuration
    public static final int TIMER_MAX = 10;

    // panel size
    public static final int RACER_STAT_PANEL_WIDTH = 140;
    public static final int RACER_STAT_PANEL_LABEL_WIDTH = 40;

    public static final int MIN_RACE_LENGTH = 3;
    public static final int MAX_RACE_LENGTH = 26;
    public static final int INIT_POSITION = 1;

    // color theme button configuration
    public static class ColorButtonConfig {
        public static final int COLOR_BUTTON_SIZE = 12;
        public static final int COLOR_BUTTON_MARGIN_LR = 0;
        public static final int COLOR_BUTTON_MARGIN_TB = 0;
        public static final int NUMBER_OF_BUTTONS = 15;
    }

    private static class Palette {
        public static final Color RED = new Color(0xEF5350);           //1
        public static final Color PINK = new Color(0xEC407A);          //2
        public static final Color PURPLE = new Color(0xAB47BC);        //3
        public static final Color DARK_PURPLE = new Color(0x7E57C2);   //4
        public static final Color INDIGO = new Color(0x5C6BC0);        //5
        public static final Color BLUE = new Color(0x42A5F5);          //6
        public static final Color CYAN = new Color(0x26C6DA);          //7
        public static final Color TEAL = new Color(0x26A69A);          //8
        public static final Color GREEN = new Color(0x66BB6A);         //9
        public static final Color LIGHT_GREEN = new Color(0x9CCC65);   //10
        public static final Color LIME = new Color(0xD4E157);          //11
        public static final Color AMBER = new Color(0xFFCA28);         //12
        public static final Color ORANGE = new Color(0xFFA726);        //13
        public static final Color DEEP_ORANGE = new Color(0xFF7043);   //14
        public static final Color BROWN = new Color(0x8D6E63);         //15
    }

    public static final List<Color> COLOR_LIST = Arrays.asList(
            Palette.RED, Palette.PINK, Palette.PURPLE, Palette.DARK_PURPLE, Palette.INDIGO,
            Palette.BLUE, Palette.CYAN, Palette.TEAL, Palette.GREEN, Palette.LIGHT_GREEN,
            Palette.LIME, Palette.AMBER, Palette.ORANGE, Palette.DEEP_ORANGE, Palette.BROWN
    );

    public static class ACTION_ON_RACER_STATUS_PANEL_FLAG {
        public static final int CREATE_FLAG = 0;
        public static final int CHANGE_THEME_FLAG = 1;
        public static final int UPDATE_POINT_FLAG = 2;
        public static final int INIT_OPPONENT_BAR_FLAG = 3;
    }
}


