package appClient;

/**
 * Class responsible for handling messages coming from the server
 */
public class GameCommunicationModule implements CommunicationModule {

    private final MainClient app;

    public GameCommunicationModule(MainClient app) {
        this.app = app;
    }

    /**
     * Method responsible for identifying and handling messages received from the server.
     * Detailed description of said messages can be found in class MainClient.
     * Each message starts with a keyword, method performs various operations depending on these keywords
     * and invokes a method that converts provided parameters to an array, if they exist.
     *
     * @param response String containing keyword or keyword and parameters to be converted
     * @return 1 if message was END and game is supposed to finish, 0 otherwise
     */
    @Override
    public int execute(String response) {
        if (response.startsWith("START")) {
            int[][] gameInfo = this.resolveParameters(response.substring(6));

            this.app.getAppWindow().setStartingBoard(gameInfo);
        }

        else if (response.startsWith("SET")) {
            int[][] gameInfo = this.resolveParameters(response.substring(4));

            for (int[] change : gameInfo) {
                this.app.getAppWindow().getBoardView().updateCellState(change[0], change[1], change[2]);
            }
        }

        else if (response.startsWith("CLEAR")) {
            this.app.getAppWindow().getBoardView().clearPossibleMoves();
        }

        else if (response.startsWith("MESSAGE")) {
            this.app.getAppWindow().setMessage(response.substring(8));
        }

        else if (response.startsWith("LOST")) {
            this.app.getAppWindow().showMessageWindow("Somebody won");
        }

        else if (response.startsWith("WON")) {
            this.app.getAppWindow().showMessageWindow("You won " + response.substring(4) + " place!");
        }

        else if (response.startsWith("LEAVE")) {
            this.app.getAppWindow().showMessageWindow("Someone left the game");
            return 1;
        }

        else if (response.startsWith("END")) {
            this.app.getAppWindow().showMessageWindow("Game Ended!");
            return 1;
        }

        return 0;
    }


}
