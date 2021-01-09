package view;

public interface AppWindow {
    void setStartingBoard(int[][] gameInfo);
    void setPlayer(int playerId);
    void setMessage(String substring);
    void showMessageWindow(String message);
    BoardView getBoardView();

    void close();
}
