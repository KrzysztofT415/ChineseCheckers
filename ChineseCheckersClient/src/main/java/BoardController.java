import view.BoardView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardController extends MouseAdapter {

    private final ClientCommunicationService communicationService;
    private final BoardView boardView;

    BoardController(ClientCommunicationService communicationService, BoardView boardView) {
        this.communicationService = communicationService;
        this.boardView = boardView;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int[] cell = this.calculateCellAtPoint((e.getX() - boardView.getBoardWidth() / 2.), (e.getY() - boardView.getBoardHeight() / 2.));
        communicationService.sendClick(cell[0], cell[1]);
    }

    private int[] calculateCellAtPoint(double x, double y) {
        double q = (-1. / 3 * y + Math.sqrt(3) / 3 * x) / (boardView.getCellRadius() + boardView.getBoardSpacing());
        double r = (2. / 3 * y) / (boardView.getCellRadius() + boardView.getBoardSpacing());
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
}
