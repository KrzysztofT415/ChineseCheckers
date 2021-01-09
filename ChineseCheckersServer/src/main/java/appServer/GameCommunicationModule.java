package appServer;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;
import games.rules.FilterRule;
import games.rules.MoveOneRule;
import games.rules.MoveRule;

import java.util.ArrayList;

public class GameCommunicationModule implements CommunicationModule {

    private final Game game;
    private final int playerId;
    private final ServerCommunicationService communicationService;
    private Integer oldX = null, oldY = null;
    private Change[] possibleMoves;

    GameCommunicationModule(int PlayerId, Game game, ServerCommunicationService communicationService) {
        this.game = game;
        this.playerId = PlayerId;
        this.communicationService = communicationService;
    }

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
