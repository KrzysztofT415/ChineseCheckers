import boards.Cell;
import boards.GameBoard;
import rules.Change;
import rules.GameRule;
import java.util.ArrayList;

public class RulesIterator implements Iterator{

    Change[] possibleMoves;
    int i = 0;

    public  RulesIterator (Change[] possibleMoves)
    {
        this.possibleMoves = possibleMoves;
    }

    public Change next()
    {
        Change change =  possibleMoves[i];
        i++;
        return change;
    }

    public boolean hasNext()
    {
        if (i >= possibleMoves.length || possibleMoves[i] == null)
            return false;
        else
            return true;
    }

}
