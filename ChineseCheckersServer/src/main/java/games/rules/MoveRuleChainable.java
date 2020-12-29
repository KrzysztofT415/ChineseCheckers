package games.rules;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;

public interface MoveRuleChainable extends MoveRule {
    Change[] getPossibleMoves(Cell currentCell, GameBoard board, Change[] previousChain);
}
