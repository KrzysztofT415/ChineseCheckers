package view;

/**
 * Interface that defines window capable of displaying game.
 */
public interface AppWindow {
    void setStartingBoard(int[][] gameInfo);
    void setPlayer(int playerId);
    void setMessage(String substring);
    void showMessageWindow(String message);
    BoardView getBoardView();

    void close();
}
