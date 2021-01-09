package games;

/**
 * Object containing coordinates x,y and state
 */
public class Change {
    private final int x;
    private final int y;
    private final int state;

    public Change(int x, int y, int state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    /**
     * Method to get coordinate x of this Change
     * @return Coordinate x of this Change
     */
    public int getX() {
        return x;
    }

    /**
     * Method to get coordinate y of this Change
     * @return Coordinate y of this Change
     */
    public int getY() {
        return y;
    }
    /**
     * Method to get state of this Change
     * @return State of this Change
     */
    public int getState() {
        return state;
    }
}
