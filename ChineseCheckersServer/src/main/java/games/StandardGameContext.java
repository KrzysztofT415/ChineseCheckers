package games;

import games.boards.GameBoard;
import games.boards.StandardBoard;
import games.rules.GameRule;
import games.rules.MoveOneRule;
import games.rules.NotLeaveFinalCornerRule;
import games.rules.SmallJumpRule;

/**
 * Class stores information about used board and rules
 */
public class StandardGameContext implements GameContext {
    private final GameRule[] rules;
    private final GameBoard board;

    public StandardGameContext() {
        this.rules = new GameRule[] {
                new MoveOneRule(),
                new SmallJumpRule(),
                new NotLeaveFinalCornerRule()
        };
        this.board = new StandardBoard();
    }

    /**
     * Method to get all rules used this gameContext
     * @return All rules used in this gameContext
     */
    @Override
    public GameRule[] getRules() {
        return rules;
    }

    /**
     * Method to get the board used in this gameContext
     * @return Board used in this gameContext
     */
    @Override
    public GameBoard getBoard() {
        return board;
    }
}
