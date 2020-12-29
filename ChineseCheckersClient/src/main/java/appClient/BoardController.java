package appClient;

import view.BoardView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardController extends MouseAdapter {

    private final ClientCommunicationService communicationService;
    private final BoardView boardView;

    public BoardController(ClientCommunicationService communicationService, BoardView boardView) {
        this.communicationService = communicationService;
        this.boardView = boardView;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int[] cell = this.boardView.calculateCellAtPoint(e.getX(), e.getY());
        this.communicationService.sendClick(cell[0], cell[1]);
    }
}
