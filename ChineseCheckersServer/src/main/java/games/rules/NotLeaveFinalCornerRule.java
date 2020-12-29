package games.rules;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;

import java.util.ArrayList;

public class NotLeaveFinalCornerRule implements FilterRule {
    @Override
    public Change[] filterMoves(Cell currentCell, GameBoard board, Change[] changes) {
        if (board.getCorner(currentCell.getX(), currentCell.getY()) != board.getDestination(currentCell.getCellState())) {
            return changes;
        }

        ArrayList<Change> filteredMoves = new ArrayList<>();
        for (Change change : changes) {
            if (board.getCorner(change.getX(), change.getY()) == board.getDestination(currentCell.getCellState())) {
                filteredMoves.add(change);
            }
        }

        return filteredMoves.toArray(new Change[0]);
    }
}
