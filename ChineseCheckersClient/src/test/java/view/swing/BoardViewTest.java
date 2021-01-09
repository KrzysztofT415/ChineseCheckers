package view.swing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import view.swing.SwingBoardView;

public class BoardViewTest {

    SwingBoardView boardView;

    @Before
    public void setUp() {
        int[][] dummyGameInfo = {{2, 0, 2}, {1, 0, 1}};
        boardView = new SwingBoardView(dummyGameInfo);
    }

    @Test
    public void testCalculateCellAtPoint() {
        int[] expected = {-1,-1};
        Assert.assertArrayEquals(expected, boardView.calculateCellAtPoint(300.3,300.7));
    }


}
