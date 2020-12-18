package rules;

import boards.Cell;
import boards.GameBoard;

public interface FilterRule extends GameRule {
    Change[] filterMoves(Cell currentCell, GameBoard board, Change[] changes);
}
