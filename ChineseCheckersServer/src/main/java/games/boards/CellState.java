package games.boards;

/**
 * Defines all possible states of cell. Each state has unique id.
 * Could be replaced by hashMap, because has similar purpose.
 */
public enum CellState {
    EMPTY(0),
    PLAYER1(1),
    PLAYER2(2),
    PLAYER3(3),
    PLAYER4(4),
    PLAYER5(5),
    PLAYER6(6);

    private final int cellStateId;
    CellState(int cellStateId) {
        this.cellStateId = cellStateId;
    }

    public int getCellStateId() {
        return cellStateId;
    }
}
