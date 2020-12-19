import games.rules.Change;

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
        return i < possibleMoves.length && possibleMoves[i] != null;
    }

}
