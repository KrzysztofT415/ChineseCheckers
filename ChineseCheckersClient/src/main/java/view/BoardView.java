package view;

/**
 * Interface that defines displaying and managing board.
 */
public interface BoardView {
    int[] calculateCellAtPoint(double x, double y);
    void updateCellState(int x, int y, int newState);
    void clearPossibleMoves();
}
