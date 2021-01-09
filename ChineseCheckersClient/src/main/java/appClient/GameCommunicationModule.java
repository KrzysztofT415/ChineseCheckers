package appClient;

public class GameCommunicationModule implements CommunicationModule {

    private final MainClient app;

    public GameCommunicationModule(MainClient app) {
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

    public int[][] resolveParameters(String response) {
        String[] stringParameters = response.split(";");

        int[][] changes = new int[Integer.parseInt(stringParameters[0])][3];
        for (int i = 0; i < changes.length; i++) {
            changes[i] = new int[]
                    { Integer.parseInt(stringParameters[i * 3 + 1]),
                            Integer.parseInt(stringParameters[i * 3 + 2]),
                            Integer.parseInt(stringParameters[i * 3 + 3]) };
        }

        return changes;
    }
}
