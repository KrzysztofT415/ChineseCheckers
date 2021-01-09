package games.rules;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;
/**
 * Interface for classes responsible for generating array of possible moves according to some rule
 */
public interface MoveRule extends GameRule {
    /**
     *
     * @param currentCell Cell for which method generates possible moves
     * @param board Currently used GameBoard
     * @return Array of objects of type Change containing coordinates x,y and their state
     * indicating that the player can move there
     */
    Change[] getPossibleMoves(Cell currentCell, GameBoard board);
}
