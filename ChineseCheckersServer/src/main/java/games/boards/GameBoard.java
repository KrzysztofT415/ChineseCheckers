package games.boards;

public interface GameBoard {
    Cell getCell(int x, int y);
    int getCellState(int x, int y);
    void setCellState(int x, int y, int newState);
    void placePlayers(int numberOfPlayers);
    boolean isWinner(int playerId);
    int getDestination(int playerId);
    int getCorner(int x, int y);
    int[][] asGameInfo();
    }
