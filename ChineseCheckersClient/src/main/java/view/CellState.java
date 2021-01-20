package view;

import java.awt.*;

/**
 * Defines all possible states of cells' display. Each state has its own color.
 */
public enum CellState {
    EMPTY(new Color(205,221,221)),
    PLAYER1(new Color(165,68,39)),
    PLAYER2(new Color(0,110,144)),
    PLAYER3(new Color(241,143,1)),
    PLAYER4(new Color(33,78,52)),
    PLAYER5(new Color(171,54,143)),
    PLAYER6(new Color(120, 84, 61)),
    POSSIBLE_MOVE(new Color(255,236,81)),
    HIGHLIGHT(new Color(255, 71, 71, 40)),
    WHITE(Color.white);

    private final Color color;

    CellState(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Gets state using id.
     * @param id id of state to search for
     * @return state with this id or white by default
     */
    public static CellState getStateById(int id) {
        switch (id) {
            case 0:
                return EMPTY;
            case 1:
                return PLAYER1;
            case 2:
                return PLAYER2;
            case 3:
                return PLAYER3;
            case 4:
                return PLAYER4;
            case 5:
                return PLAYER5;
            case 6:
                return PLAYER6;
            case 7:
                return POSSIBLE_MOVE;
            default:
                return WHITE;
        }
    }
}
