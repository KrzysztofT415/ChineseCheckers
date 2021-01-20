package view;

import view.swing.SwingCellView;

/**
 * Interface that defines displaying and managing board.
 */
public interface BoardView {
    int[] calculateCellAtPoint(double x, double y);

    void highlight(int x, int y);
    void updateCellState(int x, int y, int newState);
    void clearPossibleMoves();

    void refresh();
}
