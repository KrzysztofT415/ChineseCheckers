package games.rules;

import games.boards.Cell;
import games.boards.GameBoard;

public interface FilterRule extends GameRule {
    Change[] filterMoves(Cell currentCell, GameBoard board, Change[] changes);
}
