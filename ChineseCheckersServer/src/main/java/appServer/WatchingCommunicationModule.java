package appServer;

import appServer.connectionDB.GameEntry;
import appServer.connectionDB.GameJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class WatchingCommunicationModule implements CommunicationModule {

    private int gameId;
    private GameJDBCTemplate gameJDBCTemplate;

    private ServerCommunicationService communicationService;
    private final ApplicationContext context;

    private StringBuilder startingBoard;

    private List<GameEntry> gameEntries;

    private int startingEntryId = 0;
    private int lastEntryId;
    private int currentEntryId;

    public void setCommunicationService(ServerCommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setGameJDBCTemplate(GameJDBCTemplate gameJDBCTemplate) {
        this.gameJDBCTemplate = gameJDBCTemplate;
    }

    WatchingCommunicationModule(int gameId, ServerCommunicationService communicationService) {
        this.gameId = gameId;
        this.communicationService = communicationService;
        this.context = new ClassPathXmlApplicationContext("Beans.xml");
        this.gameJDBCTemplate = (GameJDBCTemplate) context.getBean("gameJDBCTemplate");
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
            this.communicationService.send("CLEAR");
            this.communicationService.send(this.move(1));
        } else if (command.startsWith("BACK")) {
            this.communicationService.send("CLEAR");
            this.communicationService.send(this.move(-1));
        } else if (command.startsWith("QUIT")) {
            return 1;
        }
        return 0;
    }

    private String move(int i) {
        this.currentEntryId += i;
        if (i < 0) {
            if (currentEntryId < startingEntryId - 1) {
                this.communicationService.send("BEGIN");
                currentEntryId = startingEntryId - 1; }
            GameEntry ge = gameEntries.get(currentEntryId + 1);
            return "SET 2;" + ge.getX1() + ";" + ge.getY1() + ";" + ge.getPlayerId() + ";" + ge.getX2() + ";" + ge.getY2() + ";7";
        } else {
            if (currentEntryId > lastEntryId) {
                this.communicationService.send("END");
                currentEntryId = lastEntryId; }
            GameEntry ge = gameEntries.get(currentEntryId);
            return "SET 2;" + ge.getX1() + ";" + ge.getY1() + ";7;" + ge.getX2() + ";" + ge.getY2() + ";" + ge.getPlayerId();
        }
    }
}
