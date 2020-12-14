import rules.Change;

public class RulesIterator implements Iterator{
    Change[] possibleMoves;
    int i = 0;

    public  RulesIterator (Change[] possibleMoves)
    {
        this.possibleMoves = possibleMoves;
    }

    public Object next()
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
