package rules;

import boards.Cell;
import boards.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SmallJumpRule implements MoveRule {
    @Override
    public Change[] getPossibleMoves(Cell currentCell, GameBoard board) {

        ArrayList<Change> possibleMoves = new ArrayList<>();

        for (int[] direction : Cell.directions) {
            int x = currentCell.getX() + direction[0];
            int y = currentCell.getY() + direction[1];

            if (board.getCellState(x, y) != 0 && board.getCellState(x, y) != -1) {
                Cell destinationCell = board.getCell(x + direction[0], y + direction[1]);
                if (destinationCell != null) {
                    if (!isCounted(destinationCell, possibleMoves) && destinationCell.getCellState() != 0) {
                        possibleMoves.add(new Change(x + direction[0], y + direction[1], 7));
                        Change[] furtherMoves = this.getPossibleMoves(destinationCell, board);
                        possibleMoves.addAll(Arrays.asList(furtherMoves));
                    }
                }
            }
        }

        return possibleMoves.toArray(new Change[0]);
    }

    private boolean isCounted(Cell destinationCell, ArrayList<Change> possibleMoves) {
        for (Change possibleMove : possibleMoves) {
            if (possibleMove.getX() == destinationCell.getX() && possibleMove.getY() == destinationCell.getY()) {
                return true;
            }
        }
        return false;
    }
}
