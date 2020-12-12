package rules;

public class Change {
    private final int x;
    private final int y;
    private final int state;

    public Change(int x, int y, int state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getState() {
        return state;
    }
}
