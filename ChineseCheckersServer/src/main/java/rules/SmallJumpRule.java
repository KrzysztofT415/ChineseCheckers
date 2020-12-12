package rules;

import boards.Cell;
import boards.GameBoard;

import java.util.ArrayList;
import java.util.Collections;

public class SmallJumpRule implements GameRule {
    @Override
    public Change[] getPossibleMoves(Cell currentCell, GameBoard board) {

        ArrayList<Change> possibleMoves = new ArrayList<>();

        for (int[] direction : Cell.directions) {
            int x = currentCell.getX() + direction[0];
            int y = currentCell.getY() + direction[1];

            if (board.getCellState(x, y) != 0 && board.getCellState(x + direction[0], y + direction[1]) == 0) {
                possibleMoves.add(new Change(x + direction[0], y + direction[1], 7));

                Change[] furtherMoves = this.getPossibleMoves(board.getCell(x + direction[0], y + direction[1]), board);
                Collections.addAll(possibleMoves, furtherMoves);
            }
        }

        return possibleMoves.toArray(new Change[0]);
    }
}
