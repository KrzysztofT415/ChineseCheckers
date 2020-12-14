package boards;

import java.util.ArrayList;

public class StandardBoard implements GameBoard {

    private final Cell[] centerCells;
    private final Cell[][] cornerCells;
    private int[] playerPlacing;

    public StandardBoard() {

        ArrayList<Cell> cellsStack = new ArrayList<>();

        // Generating empty center
        int r = 4;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if (x + y + z == 0) {
                        cellsStack.add(new Cell(x, z, 0));
                    }
                }
            }
        }
        this.centerCells = cellsStack.toArray(new Cell[0]);
        cellsStack.clear();

        //Generating empty corners
        this.cornerCells = new Cell[6][10];
        int[][] elements = new int[][] { {1,4,-5}, {2,3,-5}, {3,2,-5}, {4,1,-5}, {2,4,-6}, {3,3,-6}, {4,2,-6}, {3,4,-7}, {4,3,-7}, {4,4,-8} };

        for (int i = 0; i < 6; i++) {
            for (int[] element : elements) {

                Cell newCell;
                switch (i) {
                    case 0:
                        newCell = new Cell(element[0], element[2], 0);
                        break;
                    case 1:
                        newCell = new Cell(-element[1], -element[0], 0);
                        break;
                    case 2:
                        newCell = new Cell(element[2], element[1], 0);
                        break;
                    case 3:
                        newCell = new Cell(-element[0], -element[2], 0);
                        break;
                    case 4:
                        newCell = new Cell(element[1], element[0], 0);
                        break;
                    default:
                        newCell = new Cell(-element[2], -element[1], 0);
                        break;
                }

                cellsStack.add(newCell);
            }

            this.cornerCells[i] = cellsStack.toArray(new Cell[0]);
            cellsStack.clear();
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
    public int getCellState(int x, int y) {
        return this.getCell(x, y).getCellState();
    }

    @Override
    public void setCellState(int x, int y, int newState) {
        this.getCell(x, y).setCellState(newState);
    }

    @Override
    public void placePlayers(int numberOfPlayers) {
        switch (numberOfPlayers) {
            case 2:
                playerPlacing = new int[] {0,3};
                break;
            case 3:
                playerPlacing = new int[] {0,2,4};
                break;
            case 4:
                playerPlacing = new int[] {1,2,4,5};
                break;
            default:
                playerPlacing = new int[] {0,1,2,3,4,5};
                break;
        }

        for (int i = 0; i < playerPlacing.length; i++) {
            for (Cell cornerCell : cornerCells[playerPlacing[i]]) {
                cornerCell.setCellState(i + 1);
            }
        }
    }

    @Override
    public int hasWinner() {
        for (int i = 0; i < playerPlacing.length; i++) {
            for (Cell cell : cornerCells[(playerPlacing[i] + 3) % 6]) {
                if (cell.getCellState() != i + 1) {
                    break;
                }
                return i;
            }
        }
        return 0;
    }

    @Override
    public int[][] asGameInfo() {
        ArrayList<int[]> cellInfo = new ArrayList<>();
        for (Cell cell : centerCells) {
            cellInfo.add(new int[] {cell.getX(), cell.getY(), cell.getCellState()});
        }
        for (Cell[] corner : cornerCells) {
            for (Cell cell : corner) {
                cellInfo.add(new int[] {cell.getX(), cell.getY(), cell.getCellState()});
            }
        }
        return cellInfo.toArray(new int[0][]);
    }

}
