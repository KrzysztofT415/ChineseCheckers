package games.rules;

import games.boards.Cell;
import games.boards.GameBoard;

public interface MoveRule extends GameRule {
    Change[] getPossibleMoves(Cell currentCell, GameBoard board);
}
