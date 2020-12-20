package view;

import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {

    private final int BOARD_WIDTH = 720;
    private final int BOARD_HEIGHT = 720;
    private final int BOARD_SPACING = 5;
    private final int CELL_RADIUS = 20;

    private final CellView[] allCells;

    public BoardView(int[][] gameInfo) {

        this.allCells = new CellView[gameInfo.length];
        for (int i = 0; i < gameInfo.length; i++) {

            //Calculating cell center
            double x = BOARD_WIDTH / 2. + (gameInfo[i][0] + gameInfo[i][1] / 2.) * (CELL_RADIUS + BOARD_SPACING) * Math.sqrt(3);
            double y = BOARD_HEIGHT / 2. + gameInfo[i][1] * (CELL_RADIUS + BOARD_SPACING) * 1.5;

            this.allCells[i] = new CellView((int) x,(int) y, gameInfo[i][0], gameInfo[i][1], gameInfo[i][2], CELL_RADIUS);
        }

        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public void updateCellState(int x, int y, int newState) {
        for (CellView cell : allCells) {
            if (cell.getQ() == x && cell.getR() == y) {
                cell.setCellState(newState);
                this.paintCell(cell, (Graphics2D) this.getGraphics());
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(new Color(172,184,186));
        g2d.fillRect(0,0, BOARD_WIDTH, BOARD_HEIGHT);

        for (CellView cell : allCells) {
            this.paintCell(cell, g2d);
        }
    }

    private void paintCell(CellView cell, Graphics2D g2d) {
        if (cell.getColorState() != CellState.POSSIBLE_MOVE) {
            g2d.setPaint(CellState.EMPTY.getColor());
        } else {
            g2d.setPaint(CellState.POSSIBLE_MOVE.getColor());
        }
        g2d.fillPolygon(cell);

        if (cell.getColorState() != CellState.EMPTY || cell.getColorState() != CellState.POSSIBLE_MOVE) {
            g2d.setPaint(cell.getColor());
            g2d.fillOval((int) (cell.getX() - (CELL_RADIUS * 0.7) - 1), (int) (cell.getY() - (CELL_RADIUS * 0.7) - 1), (int) (CELL_RADIUS * 1.4), (int) (CELL_RADIUS * 1.4));
        }
    }

    public void removePossibleMoves() {
        for (CellView cell : allCells) {
            if (cell.getColorState() == CellState.POSSIBLE_MOVE) {
                cell.setCellState(0);
                this.paintCell(cell, (Graphics2D) this.getGraphics());
            }
        }
    }

    public int getBoardWidth() {
        return BOARD_WIDTH;
    }

    public int getBoardHeight() {
        return BOARD_HEIGHT;
    }

    public int getBoardSpacing() {
        return BOARD_SPACING;
    }

    public int getCellRadius() {
        return CELL_RADIUS;
    }
}
