package games;

import games.boards.GameBoard;
import games.rules.FilterRule;
import games.rules.GameRule;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Interface for classes storing information about used rules and board
 */
public interface GameContext {
    /**
     * Method returns array of used rules
     * @return Array of used rules
     */
    GameRule[] getRules();

    /**
     * Method returns used board
     * @return Used board
     */
    GameBoard getBoard();

    default <T extends GameRule> ArrayList<T> getRulesOfType(Class<T> searchedClass) {
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
