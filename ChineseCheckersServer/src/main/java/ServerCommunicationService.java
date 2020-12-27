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
                    game.resend("MESSAGE Player "+playerId+" passed his turn", playerId);
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

    private void processMoveCommand(int chosenX, int chosenY) {

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

    private Change[] getPossibleMoves(int chosenX, int chosenY) {
        //TODO : get all possible move
        ArrayList<Change> list = new ArrayList<>();
        Iterator gameRulesIterator = new GameRulesIterator(this.game.getGameContext().getRules());

        while (gameRulesIterator.hasNext()) {
            Iterator iterator = new RulesIterator(((MoveRule) gameRulesIterator.next()).getPossibleMoves(this.game.getGameContext().getBoard().getCell(chosenX, chosenY), this.game.getGameContext().getBoard()));
//            Iterator iterator;
//            if (gameRulesIterator.next() instanceof MoveRule) {
//                iterator = new RulesIterator(((MoveRule) gameRulesIterator.next()).getPossibleMoves(this.game.getGameContext().getBoard().getCell(chosenX, chosenY), this.game.getGameContext().getBoard()));
//            } else {
//                iterator = new RulesIterator(((FilterRule) gameRulesIterator.next()).filterMoves(this.game.getGameContext().getBoard().getCell(chosenX, chosenY), this.game.getGameContext().getBoard()));
//            }
            while (iterator.hasNext()) {
                list.add((Change) iterator.next());
            }
        }
        return list.toArray(new Change[0]);
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
