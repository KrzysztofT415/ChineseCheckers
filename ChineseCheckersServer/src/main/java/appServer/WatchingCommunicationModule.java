package appServer;

import appServer.connectionDB.GameEntry;
import appServer.connectionDB.GameJDBCTemplate;

import java.util.List;

public class WatchingCommunicationModule implements CommunicationModule {

    private final int gameId;
    private final GameJDBCTemplate gameJDBCTemplate;
    private final ServerCommunicationService communicationService;

    private StringBuilder startingBoard;

    private final List<GameEntry> gameEntries;

    private int startingEntryId = 0;
    private final int lastEntryId;
    private int currentEntryId;

    WatchingCommunicationModule(int gameId, GameJDBCTemplate gameJDBCTemplate, ServerCommunicationService communicationService) {
        this.gameId = gameId;
        this.gameJDBCTemplate = gameJDBCTemplate;
        this.communicationService = communicationService;
        this.gameEntries = gameJDBCTemplate.getGameInformation(gameId);

        for (int i = 0; i < gameEntries.size(); i++) {
            if (gameEntries.get(i).getPlayerId() > 0) {
                this.startingEntryId = i;
                this.currentEntryId = i - 1;
                break;
            }
        }

        this.lastEntryId = gameEntries.size() - 1;
        this.setStartingBoard();
    }

    private void setStartingBoard() {
        startingBoard = new StringBuilder();
        int counter = 0;
        for (GameEntry gameEntry : gameEntries) {
            if (gameEntry.getPlayerId() <= 0) {
                startingBoard.append(";")
                        .append(gameEntry.getX2())
                        .append(";")
                        .append(gameEntry.getY2())
                        .append(";")
                        .append(-gameEntry.getPlayerId());
                counter++;
            }
        }

        this.startingBoard.insert(0, "START " + counter);
    }

    public StringBuilder getStartingBoard() {
        return startingBoard;
    }

    @Override
    public int execute(String command) {
        if (command.startsWith("NEXT")) {
            this.communicationService.send(this.move(1));
        } else if (command.startsWith("BACK")) {
            this.communicationService.send(this.move(-1));
        } else if (command.startsWith("QUIT")) {
            return 1;
        }
        return 0;
    }

    private String move(int i) {
        this.currentEntryId += i;
        if (i < 0) {
            if (currentEntryId < startingEntryId - 1) { currentEntryId = startingEntryId - 1; }
            GameEntry ge = gameEntries.get(currentEntryId + 1);
            return "SET 2;" + ge.getX1() + ";" + ge.getY1() + ";" + ge.getPlayerId() + ";" + ge.getX2() + ";" + ge.getY2() + ";0";
        } else {
            if (currentEntryId > lastEntryId) { currentEntryId = lastEntryId; }
            GameEntry ge = gameEntries.get(currentEntryId);
            return "SET 2;" + ge.getX1() + ";" + ge.getY1() + ";0;" + ge.getX2() + ";" + ge.getY2() + ";" + ge.getPlayerId();
        }
    }
}
