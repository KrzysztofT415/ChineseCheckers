package games.boards;

public class StandardBoard implements GameBoard {

    private final Cell[] centerCells;
    private final Cell[][] cornerCells;
    private int[] playerPlacing;

    public StandardBoard() {
        this.centerCells = new Cell[61];

        // Generating empty center
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

        //Generating empty corners
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

    @Override
    public int getCellState(int x, int y) {
        Cell chosenCell = this.getCell(x, y);
        if (chosenCell == null) {
            return -1;
        }
        return chosenCell.getCellState();
    }

    @Override
    public void setCellState(int x, int y, int newState) {
        this.getCell(x, y).setCellState(newState);
    }

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

    @Override
    public boolean isWinner(int playerId) {
        for (Cell cell : cornerCells[getDestination(playerId)]) {
            if (cell.getCellState() != playerId) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getDestination(int playerId) {
        return (playerPlacing[playerId - 1] + 3) % 6;
    }

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
