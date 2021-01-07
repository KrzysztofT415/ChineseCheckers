package games.rules;

import games.*;
import games.boards.*;
import org.junit.*;

public class RulesTest {

    MoveOneRule moveOneRule;
    SmallJumpRule smallJumpRule;
    GameBoard gameBoard;
    FilterRule filterRule;

    @Before
    public void setUp(){
        gameBoard = new StandardBoard();
        moveOneRule = new MoveOneRule();
        smallJumpRule = new SmallJumpRule();
        filterRule = new NotLeaveFinalCornerRule();
    }

    private boolean compareChangeArrays(Change[] array1, Change[] array2) {
        if (array1.length == array2.length) {
            for (int i = 0; i < array1.length; i++) {
                if (array1[i].getX() != array2[i].getX() || array1[i].getY() != array2[i].getY()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Test
    public void testMoveOneRule() {
        Cell cell = new Cell(0,0,0);
        Change[] change = {
                new Change(1,0,7), new Change(0,1,7), new Change(-1,1,7),
                new Change(-1,0,7), new Change(0,-1,7), new Change(1,-1,7)};

        Assert.assertTrue(compareChangeArrays(change, moveOneRule.getPossibleMoves(cell, gameBoard)));
    }

    @Test
    public void testSmallJumpRule() {
        Cell cell = new Cell(0,0,2);
        gameBoard.setCellState(0, 0,2);
        gameBoard.setCellState(1, 0,3);
        Change[] change = {
                new Change(0,0,7), new Change(2,0,7), new Change(0,0,7),
                new Change(2,0,7)};
        Assert.assertTrue(compareChangeArrays(change, smallJumpRule.getPossibleMoves(cell, gameBoard)));
    }

    @Test
    public void testFinalCornerRuleNoFilter() {
        Cell cell = new Cell(0,0,2);
        gameBoard.setCellState(0, 0,2);
        gameBoard.placePlayers(2);
        Change[] changes = {
                new Change(0,2,0), new Change(2,2,0) };
        Assert.assertTrue(compareChangeArrays(changes, filterRule.filterMoves(cell, gameBoard, changes)));
    }

    @Test
    public void testFinalCornerRuleWithFilter() {
        Cell cell = new Cell(-3,5,2);
        gameBoard.placePlayers(2);
        gameBoard.setCellState(-3, 5,2);
        Change[] changes = moveOneRule.getPossibleMoves(cell, gameBoard);
        Assert.assertTrue(compareChangeArrays(new Change[]{}, filterRule.filterMoves(cell, gameBoard, changes)));
    }

}
