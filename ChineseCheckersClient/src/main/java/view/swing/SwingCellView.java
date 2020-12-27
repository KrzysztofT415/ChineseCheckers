package view.swing;

import view.CellState;

import java.awt.*;

public class SwingCellView extends Polygon {

    private final int centerX;
    private final int centerY;
    private final int gridX;
    private final int gridY;
    private CellState cellState;

    public SwingCellView(int centerX, int centerY, int gridX, int gridY, int cellState, int radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.gridX = gridX;
        this.gridY = gridY;

        //Calculating hex corners
        for (int i = 0; i < 6; ++i) {
            int angle_deg = 60 * i - 30;
            double angle_rad = Math.PI / 180 * angle_deg;
            double x1 = centerX + radius * (Math.cos(angle_rad));
            double y1 = centerY + radius * (Math.sin(angle_rad));
            this.addPoint((int) x1, (int) y1);
        }

        this.setCellState(cellState);
    }

    public int getX() {
        return centerX;
    }

    public int getY() {
        return centerY;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public Color getColor() {
        return cellState.getColor();
    }

    public CellState getCellState() {
        return cellState;
    }

    public void setCellState(int cellState) {
        this.cellState = CellState.getStateById(cellState);
    }
}
