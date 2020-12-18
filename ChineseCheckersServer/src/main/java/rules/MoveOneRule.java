package rules;

import boards.Cell;
import boards.GameBoard;

import java.util.ArrayList;

public class MoveOneRule implements MoveRule {
    @Override
    public Change[] getPossibleMoves(Cell currentCell, GameBoard board) {

        ArrayList<Change> possibleMoves = new ArrayList<>();

        for (int[] direction : Cell.directions) {
            int x = currentCell.getX() + direction[0];
            int y = currentCell.getY() + direction[1];

            if (board.getCellState(x, y) == 0) {
                possibleMoves.add(new Change(x, y, 7));
            }
        }

        return possibleMoves.toArray(new Change[0]);
    }
}
