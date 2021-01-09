package games.boards;

/**
 * Standard six-pointed star shaped board.
 * Has 121 cells, 61 in center and 60 in corners.
 */
public class StandardBoard implements GameBoard {

    private final Cell[] centerCells;
    private final Cell[][] cornerCells;
    private int[] playerPlacing;

    /**
     * Default constructor.
     */
    public StandardBoard() {
        this.centerCells = new Cell[61];

        int r = 4;
        int pointer = -1;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if (x + y + z == 0) {
                        this.centerCells[++pointer] = new Cell(x, z, 0);
                    }
                }
            }
        }

        this.cornerCells = new Cell[6][10];
        int[][] elements = new int[][] { {1,4,-5}, {2,3,-5}, {3,2,-5}, {4,1,-5}, {2,4,-6}, {3,3,-6}, {4,2,-6}, {3,4,-7}, {4,3,-7}, {4,4,-8} };

        for (int i = 0; i < elements.length; i++) {
            this.cornerCells[0][i] = new Cell(elements[i][0], elements[i][2], 0);
            this.cornerCells[1][i] = new Cell(-elements[i][1], -elements[i][0], 0);
            this.cornerCells[2][i] = new Cell(elements[i][2], elements[i][1], 0);
            this.cornerCells[3][i] = new Cell(-elements[i][0], -elements[i][2], 0);
            this.cornerCells[4][i] = new Cell(elements[i][1], elements[i][0], 0);
            this.cornerCells[5][i] = new Cell(-elements[i][2], -elements[i][1], 0);
        }
    }

    /**
     * Finds cell by given coordinates.
     * @param x coordinate x of searched cell
     * @param y coordinate y of searched cell
     * @return found cell or null board has no cell with given coordinates
     */
    @Override
    public Cell getCell(int x, int y) {
        for (Cell cell : centerCells) {
            if (cell.getX() == x && cell.getY() == y) {
                return cell;
            }
        }

        for (int i = 0; i < 6; i++) {
            for (Cell cell : cornerCells[i]) {
                if (cell.getX() == x && cell.getY() == y) {
                    return cell;
                }
            }
        }

        return null;
    }

    /**
     * Finds if cell is located in corner by given coordinates.
     * @param x coordinate x of searched cell
     * @param y coordinate y of searched cell
     * @return found id of corner or 0 if cell is not found
     */
    @Override
    public int getCorner(int x, int y) {
        for (int i = 0; i < 6; i++) {
            for (Cell cell : cornerCells[i]) {
                if (cell.getX() == x && cell.getY() == y) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * Locates player on board depending on the number of players.
     * @param numberOfPlayers how many players will be in game
     */
    @Override
    public void placePlayers(int numberOfPlayers) {
        switch (numberOfPlayers) {
            case 2:
                playerPlacing = new int[] {3,0};
                break;
            case 3:
                playerPlacing = new int[] {4,2,0};
                break;
            case 4:
                playerPlacing = new int[] {5,4,2,1};
                break;
            default:
                playerPlacing = new int[] {5,4,3,2,1,0};
                break;
        }

        for (int i = 0; i < playerPlacing.length; i++) {
            for (Cell cornerCell : cornerCells[playerPlacing[i]]) {
                cornerCell.setCellState(i + 1);
            }
        }
    }

    /**
     * Finds if a player with given id won the game.
     * @param playerId id of player who will be checked
     * @return boolean value if player won
     */
    @Override
    public boolean isWinner(int playerId) {
        for (Cell cell : cornerCells[getDestination(playerId)]) {
            if (cell.getCellState() != playerId) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds id of corner that player should reach in order to win.
     * @param playerId id of player whose destination will be checked
     * @return id of destination corner
     */
    @Override
    public int getDestination(int playerId) {
        return (playerPlacing[playerId - 1] + 3) % 6;
    }

    /**
     * Transforms board into array where each cell is defined by {x, y, state}.
     * @return array of all cells
     */
    @Override
    public int[][] asGameInfo() {
        int[][] gameInfo = new int[121][3];

        int pointer = -1;
        for (Cell cell : centerCells) {
            gameInfo[++pointer] = new int[] {cell.getX(), cell.getY(), cell.getCellState()};
        }
        for (Cell[] corner : cornerCells) {
            for (Cell cell : corner) {
                gameInfo[++pointer] = new int[] {cell.getX(), cell.getY(), cell.getCellState()};
            }
        }

        return gameInfo;
    }
}
