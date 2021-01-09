package games.rules;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;

import java.util.ArrayList;

/**
 * Class containing method responsible for generating array of possible moves according to the rule
 * that pawn can move one field in every direction, provided it's not occupied by another player
 */
public class MoveOneRule implements MoveRule {
    /**
     * Method generates an array of possible moves from the cell, specified by the rule that pawn can move
     * one field in every direction, provided it's not occupied by another player
     * @param currentCell Cell for which method generates possible moves
     * @param board Currently used GameBoard
     * @return Array of objects of type Change containing coordinates x,y and their state
     * indicating that the player can move there
     */
    @Override
    public Change[] getPossibleMoves(Cell currentCell, GameBoard board) {

        ArrayList<Change> possibleMoves = new ArrayList<>();

        for (int[] direction : Cell.directions) {
            int x = currentCell.getX() + direction[0];
            int y = currentCell.getY() + direction[1];

            if (board.getCellState(x, y) == 0) {
                possibleMoves.add(new Change(x, y, 7));
            }
        }

        return possibleMoves.toArray(new Change[0]);
    }
}
