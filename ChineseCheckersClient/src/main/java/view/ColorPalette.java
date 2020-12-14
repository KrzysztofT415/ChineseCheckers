package view;

import java.awt.*;

public enum ColorPalette {
    EMPTY(new Color(205,221,221)),
    PLAYER1(new Color(165,68,39)),
    PLAYER2(new Color(0,110,144)),
    PLAYER3(new Color(241,143,1)),
    PLAYER4(new Color(33,78,52)),
    PLAYER5(new Color(171,54,143)),
    PLAYER6(new Color(47,37,4)),
    POSSIBLE_MOVE(new Color(255,236,81)),
    BACKGROUND(new Color(172,184,186));

    private final Color color;
    ColorPalette(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
