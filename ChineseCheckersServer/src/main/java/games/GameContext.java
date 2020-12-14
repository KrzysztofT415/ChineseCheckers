package games;

import boards.GameBoard;
import rules.GameRule;

public interface GameContext {
    GameRule[] getRules();
    GameBoard getBoard();
}
