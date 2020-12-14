package view;

import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {

    public static final int WINDOW_WIDTH = 720;
    public static final int WINDOW_HEIGHT = 720;
    public static final int RADIUS = 20;
    public static final int SPACING = 5;

    private final CellView[] allCells;

    public BoardView(int[][] gameInfo) {
        this.allCells = new CellView[gameInfo.length];
        for (int i = 0; i < gameInfo.length; i++) {

            //Calculating hex center
            double x = WINDOW_WIDTH / 2. - (gameInfo[i][0] + gameInfo[i][1] / 2.) * (RADIUS + SPACING) * Math.sqrt(3);
            double y = WINDOW_HEIGHT / 2. + gameInfo[i][1] * (RADIUS + SPACING) * 1.5;

            this.allCells[i] = new CellView((int) x,(int) y, gameInfo[i][0], gameInfo[i][1], gameInfo[i][2]);
        }

        this.addMouseListener(new BoardController(this));
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void updateCellState(int x, int y, int newState) {
        for (CellView cell : allCells) {
            if (cell.getQ() == x && cell.getR() == y) {
                cell.setCellState(newState);
                this.paintComponent(this.getGraphics());
                return;
            }
        }
        this.paintComponent(this.getGraphics());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(ColorPalette.BACKGROUND.getColor());
        g2d.fillRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);

        for (CellView cell : allCells) {
            if (cell.getColorState() != ColorPalette.POSSIBLE_MOVE) {
                g2d.setPaint(ColorPalette.EMPTY.getColor());
            } else {
                g2d.setPaint(ColorPalette.POSSIBLE_MOVE.getColor());
            }
            g2d.fillPolygon(cell);

            if (cell.getColorState() != ColorPalette.EMPTY || cell.getColorState() != ColorPalette.POSSIBLE_MOVE) {
                g2d.setPaint(cell.getColor());
                g2d.fillOval((int) (cell.getX() - (RADIUS * 0.7) - 1), (int) (cell.getY() - (RADIUS * 0.7) - 1), (int) (RADIUS * 1.4), (int) (RADIUS * 1.4));
            }
        }
    }
}
