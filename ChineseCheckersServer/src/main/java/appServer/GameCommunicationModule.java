package appServer;

import appServer.connectionDB.GameJDBCTemplate;
import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;
import games.rules.FilterRule;
import games.rules.MoveOneRule;
import games.rules.MoveRule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

/**
 * Class responsible for handling messages from the clients
 */
public class GameCommunicationModule implements CommunicationModule {

    private Game game;
    private int playerId;
    private GameJDBCTemplate gameJDBCTemplate;
    private ServerCommunicationService communicationService;
    private Integer oldX = null, oldY = null;
    private Change[] possibleMoves;

    public void setCommunicationService(ServerCommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    public void setGameJDBCTemplate(GameJDBCTemplate gameJDBCTemplate) {
        this.gameJDBCTemplate = gameJDBCTemplate;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    private final ApplicationContext context;

    GameCommunicationModule(int PlayerId, Game game, ServerCommunicationService communicationService) {
        this.game = game;
        this.playerId = PlayerId;
        this.context = new ClassPathXmlApplicationContext("Beans.xml");
        this.gameJDBCTemplate = (GameJDBCTemplate) context.getBean("gameJDBCTemplate");
        this.communicationService = communicationService;
    }

    /**
     * Method responsible for identifying and handling messages received from clients.
     * Detailed description of said messages can be found in class MainServer.
     * Each message starts with a keyword, method performs various operations depending on these keywords
     * and invokes a method that converts provided parameters to an array, if they exist.
     *
     * @param command String containing keyword or keyword and parameters to be converted
     * @return 1 if message was QUIT and game is supposed to finish, 0 otherwise
     */
    @Override
    public int execute(String command) {
        if (command.startsWith("CLICK")) {
            if (this.game.getCurrentPlayerId() == this.playerId) {
                String[] temp = command.substring(6).split(";");
                int chosenX = Integer.parseInt(temp[0]);
                int chosenY = Integer.parseInt(temp[1]);

                this.processMoveCommand(chosenX, chosenY);
            } else {
                this.communicationService.send("MESSAGE Not your turn");
            }

        } else if (command.startsWith("PASS")) {
            if (game.getCurrentPlayerId() == playerId) {
                game.resend("MESSAGE Player "+playerId+" passed his turn", playerId);
                this.communicationService.send("MESSAGE You Passed");
                this.oldX = null;
                this.oldY = null;
                this.possibleMoves = null;
                this.game.nextPlayer();
            } else {
                this.communicationService.send("MESSAGE Not your turn");
            }

        } else if (command.startsWith("QUIT")) {
            return 1;
        }
        return 0;
    }

    /**
     * Method processes coordinates after player has clicked something on the board
     * We assume that move has two chapters: player clicked the pawn that they want to move,
     * or player clicked the destination they want to move the pawn to.
     * Firstly, method checks if there are previously clicked coordinates, if not - it's
     * chapter one and method checks if player clicked their pawn and if there are any possible moves
     * for chosen pawn, if so it stores clicked coordinates and sends message to client about
     * possible places to move from clicked cell.
     * If there stored coordinates are not null then it means it's chapter two.
     * Method checks if newly clicked coordinates are in the array of possible moves, if so
     * it updates board, sends information about the move to clients, checks if the player won
     * and changes player to the next one
     * @param chosenX coordinate x chosen by the player
     * @param chosenY coordinate y chosen by the player
     */
    public void processMoveCommand(int chosenX, int chosenY) {

        if (this.oldX == null || this.oldY == null) {
            if (this.game.getGameContext().getBoard().getCellState(chosenX, chosenY) != playerId) {
                return;
            }

            this.possibleMoves = this.getPossibleMoves(chosenX, chosenY);
            if (possibleMoves.length == 0) {
                return;
            }

            String changeString = this.changesInfoToString(possibleMoves);
            this.communicationService.send(changeString);

            oldX = chosenX;
            oldY = chosenY;
        } else {

            if (oldX == chosenX && oldY == chosenY) {
                this.oldX = null;
                this.oldY = null;
                this.communicationService.send("CLEAR");
                return;
            }

            boolean move = false;
            for (Change possibleMove : possibleMoves) {
                if (possibleMove.getX() == chosenX && possibleMove.getY() == chosenY) {
                    move = true;
                    break;
                }
            }

            if (move) {
                this.game.getGameContext().getBoard().setCellState(oldX, oldY, 0);
                this.game.getGameContext().getBoard().setCellState(chosenX, chosenY, playerId);
                String setCommand = "SET 2;" + oldX + ";" + oldY + ";0;" + chosenX + ";" + chosenY + ";" + playerId;
                this.communicationService.send(setCommand);
                this.game.resend(setCommand, playerId);
                this.communicationService.send("CLEAR");

                this.gameJDBCTemplate.save(game.getGameId(), playerId, oldX, oldY, chosenX, chosenY);

                this.communicationService.send("MESSAGE Move saved");
                this.checkWinner();
                this.oldX = null;
                this.oldY = null;
                this.game.nextPlayer();
            } else {
                this.oldX = null;
                this.oldY = null;
                this.communicationService.send("CLEAR");
                this.processMoveCommand(chosenX, chosenY);
            }

        }
    }

    /**
     * Method generates an array of places where player can move a pawn from given coordinates x,y
     * It iterates through all the rules for the given board and returns an array of all of them,
     * then filters the moves by iterating through all the rules that restrict moving to some places
     * @param chosenX given x coordinate
     * @param chosenY given y coordinate
     * @return array with information about places where pawn can move from chosen x,y coordinates
     */
    public Change[] getPossibleMoves(int chosenX, int chosenY) {
        ArrayList<Change> possibleMoves = new ArrayList<>();
        GameBoard board = this.game.getGameContext().getBoard();
        Cell currentCell = board.getCell(chosenX, chosenY);

        MoveRule[] moveRules = this.game.getGameContext().getRulesOfType(MoveRule.class).toArray(new MoveRule[0]);
        if (moveRules.length == 0) { moveRules = new MoveRule[]{new MoveOneRule()}; }

        Iterator<MoveRule> moveRulesIterator = new Iterator<>(moveRules);

        while (moveRulesIterator.hasNext()) {
            Iterator<Change> iterator = new Iterator<>((moveRulesIterator.next()).getPossibleMoves(currentCell, board));
            while (iterator.hasNext()) {
                possibleMoves.add(iterator.next());
            }
        }

        FilterRule[] filterRules = this.game.getGameContext().getRulesOfType(FilterRule.class).toArray(new FilterRule[0]);
        if (filterRules.length == 0) { return possibleMoves.toArray(new Change[0]); }

        ArrayList<Change> possibleMovesFiltered = new ArrayList<>();
        Iterator<FilterRule> filterRulesIterator = new Iterator<>(filterRules);

        while (filterRulesIterator.hasNext()) {
            Iterator<Change> iterator = new Iterator<>((filterRulesIterator.next()).filterMoves(currentCell, board, possibleMoves.toArray(new Change[0])));
            while (iterator.hasNext()) {
                possibleMovesFiltered.add(iterator.next());
            }
        }

        return possibleMovesFiltered.toArray(new Change[0]);
    }

    /**
     * Method coverts given array to String
     * @param info given array with information to convert
     * @return String with information from the array in a form of [SET length;x;y;state;x1;y1;state1;...]
     */
    public String changesInfoToString(Change[] info) {
        StringBuilder result = new StringBuilder("SET "+info.length);
        for (Change change : info) {
            result  .append(";")
                    .append(change.getX())
                    .append(";")
                    .append(change.getY())
                    .append(";")
                    .append(change.getState());
        }
        return result.toString();
    }

    /**
     * Method checks if player won the game, if so it sends information about it to
     * all the players
     */
    public void checkWinner() {
        if (game.getGameContext().getBoard().isWinner(playerId)) {
            int n = game.getWinners();
            if (n == 1) {
                this.game.resend("LOST", playerId);
            }
            this.communicationService.send("WON " + n);
        }
    }
}
