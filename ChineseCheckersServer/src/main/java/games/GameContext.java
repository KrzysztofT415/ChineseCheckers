package games;

import games.boards.GameBoard;
import games.rules.GameRule;

public interface GameContext {
    GameRule[] getRules();
    GameBoard getBoard();
}
