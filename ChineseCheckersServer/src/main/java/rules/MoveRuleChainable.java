package rules;

import boards.Cell;
import boards.GameBoard;

public interface MoveRuleChainable extends MoveRule {
    Change[] getPossibleMoves(Cell currentCell, GameBoard board, Change[] previousChain);
}
