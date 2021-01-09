package games.rules;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;

import java.util.ArrayList;

/**
 * Class contains method responsible for filtering previously generated arrays of possible
 * moves according to the rule that pawn cannot leave the corner area belonging to the opponent
 */
public class NotLeaveFinalCornerRule implements FilterRule {
    /**
     * Method filters previously generated possible moves according to the rule that if
     * player's pawn is in the opponent's sector, the pawn can't leave that area
     * @param currentCell Cell for which method generates possible moves
     * @param board Currently used GameBoard
     * @param changes Array of previously generated possible moves
     * @return Provided array changes but without places outside of the corner
     */
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
