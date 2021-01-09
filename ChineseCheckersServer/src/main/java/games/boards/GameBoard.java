package games.boards;

/**
 * Interface that defines playable board.
 * Contains factory methods same to all types of boards.
 */
public interface GameBoard {

    /**
     * Finds state of cell by given coordinates.
     * @param x coordinate x of searched cell
     * @param y coordinate y of searched cell
     * @return state of found cell or -1 if board has no cell with given coordinates
     */
    default int getCellState(int x, int y) {
        Cell chosenCell = this.getCell(x, y);
        if (chosenCell == null) {
            return -1;
        }
        return chosenCell.getCellState();
    }

    /**
     * Finds cell by given coordinates and changes its state.
     * @param x coordinate x of searched cell
     * @param y coordinate y of searched cell
     * @param newState targeted state to change cell to
     */
    default void setCellState(int x, int y, int newState) {
        this.getCell(x, y).setCellState(newState);
    }

    Cell getCell(int x, int y);
    void placePlayers(int numberOfPlayers);
    boolean isWinner(int playerId);
    int getDestination(int playerId);
    int getCorner(int x, int y);
    int[][] asGameInfo();
    }
