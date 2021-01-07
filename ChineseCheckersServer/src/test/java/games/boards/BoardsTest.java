package games.boards;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BoardsTest {

    GameBoard gameBoard;

    @Before
    public void setUp() {
        gameBoard = new StandardBoard();
    }

    @Test
    public void getCellNull() {
        Cell cellNew = gameBoard.getCell(40,50);
        Assert.assertNull(cellNew);
    }

    @Test
    public void getCornerWrong() {
        int cornerId = gameBoard.getCorner(40,50);
        Assert.assertEquals(0, cornerId);
    }

    @Test
    public void getCornerRight() {
        int cornerId = gameBoard.getCorner(4,1);
        Assert.assertEquals(4, cornerId);
    }

    @Test
    public void getCellStateRight() {
        int state = gameBoard.getCellState(0,1);
        Assert.assertEquals(0, state);
        gameBoard.setCellState(0,1,5);
        Assert.assertEquals(5, gameBoard.getCellState(0,1));
    }

    @Test
    public void getCellStateWrong() {
        int state = gameBoard.getCellState(40,50);
        Assert.assertEquals(-1, state);
    }

    @Test
    public void testPlacePlayers() {
        gameBoard.placePlayers(4);
        Assert.assertEquals(4, gameBoard.getCellState(-3, -2));
        Assert.assertEquals(2, gameBoard.getCellState(4,1));
    }

    @Test
    public void testIsWinner() {
        gameBoard.placePlayers(6);
        Assert.assertFalse(gameBoard.isWinner(2));
    }

    @Test
    public void testGetDestination() {
        gameBoard.placePlayers(6);
        Assert.assertEquals(1, gameBoard.getDestination(2));
    }

    @Test
    public void testAsGameInfo() {
        int[][] info = gameBoard.asGameInfo();
        Assert.assertEquals(121, info.length);
        Assert.assertEquals(0,info[0][2]);
        gameBoard.setCellState(info[0][0], info[0][1], 3 );
        info = gameBoard.asGameInfo();
        Assert.assertEquals(3,info[0][2]);
    }

}
