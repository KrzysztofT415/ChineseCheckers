package view.swing;

import view.BoardView;
import view.CellState;

import javax.swing.*;
import java.awt.*;

/**
 * Visual representation of board using JPanel from Swing.
 */
public class SwingBoardView extends JPanel implements BoardView {

    private final int BOARD_WIDTH = 720;
    private final int BOARD_HEIGHT = 720;
    private final int BOARD_SPACING = 5;
    private final int CELL_RADIUS = 20;

    private final SwingCellView[] allCells;

    /**
     * Default constructor. Creates board using array of cells' information.
     * @param gameInfo starting board information
     */
    public SwingBoardView(int[][] gameInfo) {

        this.allCells = new SwingCellView[gameInfo.length];
        for (int i = 0; i < gameInfo.length; i++) {

            //Calculating cell center
            double x = BOARD_WIDTH / 2. + (gameInfo[i][0] + gameInfo[i][1] / 2.) * (CELL_RADIUS + BOARD_SPACING) * Math.sqrt(3);
            double y = BOARD_HEIGHT / 2. + gameInfo[i][1] * (CELL_RADIUS + BOARD_SPACING) * 1.5;

            this.allCells[i] = new SwingCellView((int) x,(int) y, gameInfo[i][0], gameInfo[i][1], gameInfo[i][2], CELL_RADIUS);
        }

        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /**
     * Method that paints cell at board panel.
     * @param cell cell which is painted
     * @param g2d graphics where cell is painted
     */
    private void paintCell(SwingCellView cell, Graphics2D g2d) {
        if (cell.getCellState() != CellState.POSSIBLE_MOVE) {
            g2d.setPaint(CellState.EMPTY.getColor());
        } else {
            g2d.setPaint(CellState.POSSIBLE_MOVE.getColor());
        }
        g2d.fillPolygon(cell);

        if (cell.getCellState() != CellState.EMPTY || cell.getCellState() != CellState.POSSIBLE_MOVE) {
            g2d.setPaint(cell.getCellState().getColor());
            g2d.fillOval((int) (cell.getX() - (CELL_RADIUS * 0.7) - 1), (int) (cell.getY() - (CELL_RADIUS * 0.7) - 1), (int) (CELL_RADIUS * 1.4), (int) (CELL_RADIUS * 1.4));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(new Color(172,184,186));
        g2d.fillRect(0,0, BOARD_WIDTH, BOARD_HEIGHT);

        for (SwingCellView cell : allCells) {
            this.paintCell(cell, g2d);
        }
    }

    /**
     * Finds cell by given coordinates and updates its state.
     * @param x coordinate x of searched cell
     * @param y coordinate y of searched cell
     * @param newState targeted state to change cell to
     */
    @Override
    public void updateCellState(int x, int y, int newState) {
        for (SwingCellView cell : allCells) {
            if (cell.getGridX() == x && cell.getGridY() == y) {
                cell.setCellState(newState);
                this.paintCell(cell, (Graphics2D) this.getGraphics());
                return;
            }
        }
    }

    /**
     * Finds cell by given coordinates on JPanel.
     * @param x coordinate x of JPanel in pixels
     * @param y coordinate y of JPanel in pixels
     */
    @Override
    public int[] calculateCellAtPoint(double x, double y) {
        x -= BOARD_WIDTH / 2.;
        y -= BOARD_HEIGHT / 2.;

        double q = (-1. / 3 * y + Math.sqrt(3) / 3 * x) / (CELL_RADIUS + BOARD_SPACING);
        double r = (2. / 3 * y) / (CELL_RADIUS + BOARD_SPACING);
        double s = - q - r;

        double rx = Math.round(q);
        double ry = Math.round(r);
        double rz = Math.round(s);

        double dx = Math.abs(rx - q);
        double dy = Math.abs(ry - r);
        double dz = Math.abs(rz - s);

        if (dx > dy && dx > dz) {
            rx = -ry - rz;
        } else if (dy > dz) {
            ry = -rx - rz;
        }

        return new int[] {(int) rx,(int) ry};
    }

    /**
     * Finds cells that are in possible move state and changes them to empty.
     */
    @Override
    public void clearPossibleMoves() {
        for (SwingCellView cell : allCells) {
            if (cell.getCellState() == CellState.POSSIBLE_MOVE) {
                cell.setCellState(0);
                this.paintCell(cell, (Graphics2D) this.getGraphics());
            }
        }
    }
}
