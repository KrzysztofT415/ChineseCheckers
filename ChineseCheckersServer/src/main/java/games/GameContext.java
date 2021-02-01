package games;

import games.boards.GameBoard;
import games.rules.FilterRule;
import games.rules.GameRule;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Interface for classes storing information about used rules and board
 */
public class GameContext {

    private GameRule[] rules;
    private GameBoard board;

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public void setRules(GameRule[] rules) {
        this.rules = rules;
    }

    public GameRule[] getRules() {
        return rules;
    }

    public GameBoard getBoard() {
        return board;
    }

    /**
     * Method that uses generic types to filter rules and return only these which type is matching filter
     * @param searchedClass type filter
     * @return arraylist of rules with given type
     */
    public <T extends GameRule> ArrayList<T> getRulesOfType(Class<T> searchedClass) {
        ArrayList<T> rules = new ArrayList<>();
        for (GameRule rule : this.getRules()) {
            if (searchedClass.isInstance(rule)) {
                T ruleCast = searchedClass.cast(rule);
                rules.add(ruleCast);
            }
        }

        return rules;
    }
}
