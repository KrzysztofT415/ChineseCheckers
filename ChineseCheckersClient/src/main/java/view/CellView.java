package view;

import java.awt.*;

public class CellView extends Polygon {

    private final int x;
    private final int y;
    private final int q;
    private final int r;
    private CellState colorState;

    /*
    cellStates:
     0 > empty
     1-6 > occupied by according player
     7 > possible to move into
     */

    public CellView(int x, int y, int q, int r, int cellState, int radius) {
        this.x = x;
        this.y = y;
        this.q = q;
        this.r = r;

        //Calculating hex corners
        for (int i = 0; i < 6; ++i) {
            int angle_deg = 60 * i - 30;
            double angle_rad = Math.PI / 180 * angle_deg;
            double x1 = x + radius * (Math.cos(angle_rad));
            double y1 = y + radius * (Math.sin(angle_rad));
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

    public CellState getColorState() {
        return colorState;
    }

    public void setCellState(int cellState) {
        switch (cellState) {
            case 0:
                this.colorState = CellState.EMPTY;
                break;
            case 1:
                this.colorState = CellState.PLAYER1;
                break;
            case 2:
                this.colorState = CellState.PLAYER2;
                break;
            case 3:
                this.colorState = CellState.PLAYER3;
                break;
            case 4:
                this.colorState = CellState.PLAYER4;
                break;
            case 5:
                this.colorState = CellState.PLAYER5;
                break;
            case 6:
                this.colorState = CellState.PLAYER6;
                break;
            case 7:
                this.colorState = CellState.POSSIBLE_MOVE;
                break;
        }
    }
}
