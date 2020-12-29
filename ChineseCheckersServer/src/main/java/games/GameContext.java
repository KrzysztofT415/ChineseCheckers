package games;

import games.boards.GameBoard;
import games.rules.FilterRule;
import games.rules.GameRule;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface GameContext {
    GameRule[] getRules();
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
