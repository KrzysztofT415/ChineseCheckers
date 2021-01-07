package games;

import games.boards.*;
import games.rules.*;
import org.junit.Assert;
import org.junit.Test;

public class StandardGameContextTest {

    GameContext gameContext = new StandardGameContext();
    GameRule[] rules = new GameRule[] {
        new MoveOneRule(),
                new SmallJumpRule(),
                new NotLeaveFinalCornerRule()
    };
    StandardBoard board = new StandardBoard();

    @Test
    public void testGetRules() {
        Assert.assertEquals(rules[1].getClass(), gameContext.getRules()[1].getClass());
    }

    @Test
    public void testGetBoard() {
        Assert.assertEquals(board.getClass(), gameContext.getBoard().getClass());
    }
}
