package boards;

public interface GameBoard {
    Cell getCell(int x, int y);
    int getCellState(int x, int y);
    void setCellState(int x, int y, int newState);
    void placePlayers(int numberOfPlayers);
}
