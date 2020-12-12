package rules;

import boards.Cell;
import boards.GameBoard;

public interface GameRule {
    Change[] getPossibleMoves(Cell currentCell, GameBoard board);
}
