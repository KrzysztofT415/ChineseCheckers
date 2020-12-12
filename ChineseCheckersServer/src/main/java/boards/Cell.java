package boards;

public class Cell {

    public static int[][] directions = new int[][] {{1,0},{0,1},{-1,1},{-1,0},{0,-1},{1,-1}};

    private final int x;
    private final int y;
    private int cellState;

    /*
    cellStates:
     0 > empty
     1-6 > occupied by according player
     7 > possible to move into
     */

    public Cell(int x, int y, int cellState) {
        this.x = x;
        this.y = y;
        this.cellState = cellState;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCellState() {
        return cellState;
    }

    public void setCellState(int cellState) {
        this.cellState = cellState;
    }
}
