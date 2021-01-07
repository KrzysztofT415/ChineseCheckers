package appServer;

import games.rules.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IteratorTest {

    Iterator<GameRule> iterator;
    GameRule[] rules = {new MoveOneRule(), new SmallJumpRule()};

    @Before
    public void setUp() {
        iterator = new Iterator<>(rules);
    }

    @Test
    public void testNext() {

        Assert.assertEquals(MoveOneRule.class, iterator.next().getClass());
    }

    @Test
    public void testHasNext()
    {
        Assert.assertTrue(iterator.hasNext());
        GameRule rule = iterator.next();
        Assert.assertTrue(iterator.hasNext());
        rule = iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }


}
