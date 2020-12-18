package view;

import java.awt.*;

public enum CellState {
    EMPTY(new Color(205,221,221)),
    PLAYER1(new Color(165,68,39)),
    PLAYER2(new Color(0,110,144)),
    PLAYER3(new Color(241,143,1)),
    PLAYER4(new Color(33,78,52)),
    PLAYER5(new Color(171,54,143)),
    PLAYER6(new Color(47,37,4)),
    POSSIBLE_MOVE(new Color(255,236,81));

    private final Color color;
    CellState(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Color getColorById(int id) {
        switch (id) {
            case 0:
                return EMPTY.getColor();
            case 1:
                return PLAYER1.getColor();
            case 2:
                return PLAYER2.getColor();
            case 3:
                return PLAYER3.getColor();
            case 4:
                return PLAYER4.getColor();
            case 5:
                return PLAYER5.getColor();
            case 6:
                return PLAYER6.getColor();
            case 7:
                return POSSIBLE_MOVE.getColor();
            default:
                return Color.WHITE;
        }
    }
}
