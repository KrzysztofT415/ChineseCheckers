package games.rules;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;
/**
 * Interface for class responsible for filtering previously generated arrays of possible moves
 * according to some rule
 */
public interface FilterRule extends GameRule {
    /**
     * Method filters previously generated possible moves according to the rule
     * @param currentCell Cell for which method generates possible moves
     * @param board Currently used GameBoard
     * @param changes Array of previously generated possible moves
     * @return Provided array changes but without places restricted by the rule
     */
    Change[] filterMoves(Cell currentCell, GameBoard board, Change[] changes);
}
