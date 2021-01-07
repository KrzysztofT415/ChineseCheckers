package games;

import games.rules.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameContextTest {

    @Test
    public void testGetRulesOfType() {
        GameRule[] gameRulesSet = new GameRule[] {new MoveOneRule(), new SmallJumpRule(), new NotLeaveFinalCornerRule()};

        GameContext gameContextMock = mock(GameContext.class);
        when(gameContextMock.getRules()).thenReturn(gameRulesSet);
        when(gameContextMock.getRulesOfType(any())).thenCallRealMethod();

        ArrayList<MoveRule> moveRules = gameContextMock.getRulesOfType(MoveRule.class);
        Assert.assertEquals(2, moveRules.size());
        Assert.assertTrue(moveRules.get(0) instanceof MoveOneRule);
        Assert.assertTrue(moveRules.get(1) instanceof SmallJumpRule);

        ArrayList<FilterRule> filterRules = gameContextMock.getRulesOfType(FilterRule.class);
        Assert.assertEquals(1, filterRules.size());
        Assert.assertTrue(filterRules.get(0) instanceof NotLeaveFinalCornerRule);
    }
}