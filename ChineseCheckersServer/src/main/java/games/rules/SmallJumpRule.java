package games.rules;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class containing methods responsible for generating arrays of possible moves for a pawn
 * according to the rule that the pawn can jump over another pawn if they are situated
 * next to each other
 */
public class SmallJumpRule implements MoveRuleChainable {
    /**
     * Method invokes the method that generates array of possible moves according to the rule
     * @param currentCell Cell for which method generates possible moves
     * @param board GameBoard on which the pawns are situated
     * @return array of possible moves for provided cell
     */
    @Override
    public Change[] getPossibleMoves(Cell currentCell, GameBoard board) {
        return this.getPossibleMoves(currentCell, board, new Change[]{new Change(currentCell.getX(), currentCell.getY(), currentCell.getCellState())});
    }

    /**
     * Method checks if around the cell are any occupied fields, if so it checks if it's possible
     * to 'jump' over them
     * @param currentCell Cell for which method generates possible moves
     * @param board GameBoard on which the pawns are situated
     * @param possibleMovesStack Initially coordinates x,y and state of currentCell
     * @return generated array of places where pawn can move according to the rule
     */
    @Override
    public Change[] getPossibleMoves(Cell currentCell, GameBoard board, Change[] possibleMovesStack) {
        ArrayList<Change> possibleMoves = new ArrayList<>(Arrays.asList(possibleMovesStack));

        for (int[] direction : Cell.directions) {
            int x = currentCell.getX() + direction[0];
            int y = currentCell.getY() + direction[1];

            if (board.getCellState(x, y) != 0 && board.getCellState(x, y) != -1) {
                Cell destinationCell = board.getCell(x + direction[0], y + direction[1]);
                if (destinationCell != null) {
                    if (!isCounted(destinationCell, possibleMoves) && destinationCell.getCellState() == 0) {
                        possibleMoves.add(new Change(x + direction[0], y + direction[1], 7));
                        Change[] furtherMoves = this.getPossibleMoves(destinationCell, board, possibleMoves.toArray(new Change[0]));
                        possibleMoves = new ArrayList<>(Arrays.asList(furtherMoves));
                    }
                }
            }
        }

        return possibleMoves.toArray(new Change[0]);
    }

    /**
     * Method checks if given array contains given Cell's coordinates
     * @param destinationCell Cell to check
     * @param possibleMoves Array including information about coordinates x,y and states
     * @return true if array already contains information about provided cell, falase otherwise
     */
    private boolean isCounted(Cell destinationCell, ArrayList<Change> possibleMoves) {
        for (Change possibleMove : possibleMoves) {
            if (possibleMove.getX() == destinationCell.getX() && possibleMove.getY() == destinationCell.getY()) {
                return true;
            }
        }
        return false;
    }
}
