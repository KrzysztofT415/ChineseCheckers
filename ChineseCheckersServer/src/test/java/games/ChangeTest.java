package games;

import org.junit.Assert;
import org.junit.Test;

public class ChangeTest {

    Change change = new Change(1,2,3);

    @Test
    public void testGetX() {
        Assert.assertEquals(1, change.getX());
    }

    @Test
    public void testGetY() {
        Assert.assertEquals(2, change.getY());
    }

    @Test
    public void testGetState() {
        Assert.assertEquals(3, change.getState());
    }
}
