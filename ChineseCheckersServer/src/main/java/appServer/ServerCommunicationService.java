package appServer;

import games.Change;
import games.boards.Cell;
import games.boards.GameBoard;
import games.rules.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerCommunicationService {

    private final int playerId;
    private final Game game;
    private Integer oldX = null, oldY = null;
    private Change[] possibleMoves;

    private Scanner in;
    private PrintWriter out;

    public ServerCommunicationService(int playerId, Game game, Socket socket) {
        this.game = game;
        this.playerId = playerId;

        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (this.in.hasNextLine()) {
            String command = in.nextLine();
            System.out.println(playerId + " : " + command);

            if (command.startsWith("CLICK")) {
                if (this.game.getCurrentPlayerId() == this.playerId) {
                    String[] temp = command.substring(6).split(";");
                    int chosenX = Integer.parseInt(temp[0]);
                    int chosenY = Integer.parseInt(temp[1]);

                    this.processMoveCommand(chosenX, chosenY);
                } else {
                    this.out.println("MESSAGE Not your turn");
                }

            } else if (command.startsWith("PASS")) {
                if (game.getCurrentPlayerId() == playerId) {
                    game.resend("MESSAGE app.Player "+playerId+" passed his turn", playerId);
                    this.out.println("MESSAGE You Passed");
                    this.oldX = null;
                    this.oldY = null;
                    this.possibleMoves = null;
                    this.game.nextPlayer();
                } else {
                    this.out.println("MESSAGE Not your turn");
                }

            } else if (command.startsWith("QUIT")) {
                return;
            }
        }
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
            this.out.println(changeString);

            oldX = chosenX;
            oldY = chosenY;
        } else {

            if (oldX == chosenX && oldY == chosenY) {
                this.oldX = null;
                this.oldY = null;
                this.out.println("CLEAR");
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
                this.out.println(setCommand);
                this.game.resend(setCommand, playerId);
                this.out.println("CLEAR");
                this.out.println("MESSAGE Move saved");
                this.checkWinner();
                this.oldX = null;
                this.oldY = null;
                this.game.nextPlayer();
            } else {
                this.oldX = null;
                this.oldY = null;
                this.out.println("CLEAR");
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

    private String changesInfoToString(Change[] info) {
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

    private void checkWinner() {
        if (game.getGameContext().getBoard().isWinner(playerId)) {
            int n = game.getWinners();
            if (n == 1) {
                this.game.resend("LOST", playerId);
            }
            this.out.println("WON " + n);
        }
    }

    public void send(String message) {
        this.out.println(message);
    }

}
