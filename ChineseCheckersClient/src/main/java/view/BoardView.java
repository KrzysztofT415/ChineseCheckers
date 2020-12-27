package view;

public interface BoardView {
    int[] calculateCellAtPoint(double x, double y);
    void updateCellState(int x, int y, int newState);
    void clearPossibleMoves();
}
