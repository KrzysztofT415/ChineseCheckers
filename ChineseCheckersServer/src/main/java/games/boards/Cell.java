package games.boards;

/**
 * Cell on game grid.
 */
public class Cell {

    public static int[][] directions = new int[][] {{1,0},{0,1},{-1,1},{-1,0},{0,-1},{1,-1}};

    private final int x;
    private final int y;
    private CellState cellState;

    /**
     * Default cell constructor.
     * @param x coordinate x of cell on grid
     * @param y coordinate y of cell on grid
     * @param cellStateId state of cell
     */
    public Cell(int x, int y, int cellStateId) {
        this.x = x;
        this.y = y;
        this.setCellState(cellStateId);
    }

    /**
     * Changes state of cell based on id.
     * @param cellStateId id of new state
     */
    public void setCellState(int cellStateId) {
        switch (cellStateId) {
            case 0:
                this.cellState = CellState.EMPTY;
                break;
            case 1:
                this.cellState = CellState.PLAYER1;
                break;
            case 2:
                this.cellState = CellState.PLAYER2;
                break;
            case 3:
                this.cellState = CellState.PLAYER3;
                break;
            case 4:
                this.cellState = CellState.PLAYER4;
                break;
            case 5:
                this.cellState = CellState.PLAYER5;
                break;
            case 6:
                this.cellState = CellState.PLAYER6;
                break;
        }
    }

    /**
     * Returns corresponding id of state in which cell is in the moment.
     * @return id of current state
     */
    public int getCellState() {
        return cellState.getCellStateId();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
