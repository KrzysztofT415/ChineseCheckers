package appClient;

import view.BoardView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class responsible for mouse interactions with the board used by the app
 */
public class BoardController extends MouseAdapter {

    private final ClientCommunicationService communicationService;
    private final BoardView boardView;

    public BoardController(ClientCommunicationService communicationService, BoardView boardView) {
        this.communicationService = communicationService;
        this.boardView = boardView;
    }

    /**
     * Method calculates which cell was clicked by the player using coordinates provided after
     * pressing the mouse button, then invokes method responsible for sending coordinates x,y of
     * said cell to the server
     * @param e Mouse interaction with the board
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int[] cell = this.boardView.calculateCellAtPoint(e.getX(), e.getY());
        this.communicationService.send("CLICK " + cell[0] + ";" + cell[1]);
    }
}
