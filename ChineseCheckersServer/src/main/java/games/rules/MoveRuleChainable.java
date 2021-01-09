package games.rules;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;

/**
 * Interface for classes responsible for generating array of possible moves according to some rule
 */
public interface MoveRuleChainable extends MoveRule {
    Change[] getPossibleMoves(Cell currentCell, GameBoard board, Change[] previousChain);
}
