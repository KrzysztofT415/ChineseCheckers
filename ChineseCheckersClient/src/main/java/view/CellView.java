package view;

import java.awt.*;

public class CellView extends Polygon {

    private final int x;
    private final int y;
    private final int q;
    private final int r;
    private ColorPalette colorState;

    /*
    cellStates:
     0 > empty
     1-6 > occupied by according player
     7 > possible to move into
     */

    public CellView(int x, int y, int q, int r, int cellState) {
        this.x = x;
        this.y = y;
        this.q = q;
        this.r = r;

        //Calculating hex corners
        for (int i = 0; i < 6; ++i) {
            int angle_deg = 60 * i - 30;
            double angle_rad = Math.PI / 180 * angle_deg;
            double x1 = x + BoardView.RADIUS * (Math.cos(angle_rad));
            double y1 = y + BoardView.RADIUS * (Math.sin(angle_rad));
            this.addPoint((int) x1, (int) y1);
        }

        this.setCellState(cellState);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public Color getColor() {
        return colorState.getColor();
    }

    public ColorPalette getColorState() {
        return colorState;
    }

    public void setCellState(int cellState) {
        switch (cellState) {
            case 0:
                this.colorState = ColorPalette.EMPTY;
                break;
            case 1:
                this.colorState = ColorPalette.PLAYER1;
                break;
            case 2:
                this.colorState = ColorPalette.PLAYER2;
                break;
            case 3:
                this.colorState = ColorPalette.PLAYER3;
                break;
            case 4:
                this.colorState = ColorPalette.PLAYER4;
                break;
            case 5:
                this.colorState = ColorPalette.PLAYER5;
                break;
            case 6:
                this.colorState = ColorPalette.PLAYER6;
                break;
            case 7:
                this.colorState = ColorPalette.POSSIBLE_MOVE;
                break;
        }
    }
}
