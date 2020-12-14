package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardController extends MouseAdapter {

    private final BoardView boardView;
    BoardController(BoardView boardView) {
        this.boardView = boardView;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double[] hex = this.pixel_to_flat_hex((e.getX() - BoardView.WINDOW_WIDTH / 2.), (e.getY() - BoardView.WINDOW_HEIGHT / 2.));
        boardView.updateCellState((int) hex[1], (int) (- hex[1] - hex[0]), 7);
    }

    private double[] pixel_to_flat_hex(double x, double y) {
        double q = (-1. / 3 * y + Math.sqrt(3) / 3 * x) / (BoardView.RADIUS + BoardView.SPACING);
        double r = (2. / 3 * y) / (BoardView.RADIUS + BoardView.SPACING);
        return this.hex_round(new double[] {q,r});
    }

    private double[] cube_round(double[] cube) {
        double rx = Math.round(cube[0]);
        double ry = Math.round(cube[1]);
        double rz = Math.round(cube[2]);

        double x_diff = Math.abs(rx - cube[0]);
        double y_diff = Math.abs(ry - cube[1]);
        double z_diff = Math.abs(rz - cube[2]);

        if (x_diff > y_diff && x_diff > z_diff) {
            rx = -ry - rz;
        } else if (y_diff > z_diff) {
            ry = -rx - rz;
        } else {
            rz = -rx - ry;
        }
        return new double[] {rx, ry, rz};
    }

    double[] cube_to_axial(double[] cube) {
        double q = cube[0];
        double r = cube[1];
        return new double[] {q,r};
    }

    double[] axial_to_cube(double[] hex) {
        double x = hex[0];
        double z = hex[1];
        double y = -x - z;
        return new double[] {x,y,z};
    }

    double[] hex_round(double[] hex) {
        return this.cube_to_axial(this.cube_round(this.axial_to_cube(hex)));
    }
}
