package games;

import boards.GameBoard;
import boards.StandardBoard;
import rules.GameRule;
import rules.MoveOneRule;
import rules.SmallJumpRule;

public class StandardGameContext implements GameContext {
    private final GameRule[] rules;
    private final GameBoard board;

    public StandardGameContext() {
        this.rules = new GameRule[] {
                new MoveOneRule(),
                new SmallJumpRule()
        };
        this.board = new StandardBoard();
    }

    @Override
    public GameRule[] getRules() {
        return rules;
    }

    @Override
    public GameBoard getBoard() {
        return board;
    }
}
