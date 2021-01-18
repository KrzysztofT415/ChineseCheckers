package appClient;

public class WatchingCommunicationModule implements CommunicationModule {

    private final MainClient app;

    public WatchingCommunicationModule(MainClient app) {
        this.app = app;
    }

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

        else if (response.startsWith("MESSAGE")) {
            this.app.getAppWindow().setMessage(response.substring(8));
        }

        else if (response.startsWith("END")) {
            this.app.getAppWindow().setMessage("It's end of this game");
        }

        else if (response.startsWith("BEGIN")) {
            this.app.getAppWindow().setMessage("It's initial board now! Cannot go back further");
        }

        return 0;
    }
}
