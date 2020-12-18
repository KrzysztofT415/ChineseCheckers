import rules.Change;
import rules.MoveOneRule;
import rules.SmallJumpRule;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerCommunicationService {

    private final int playerId;
    private final Game game;
    private int oldX = -1, oldY = -1;
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
                    this.oldX = -1;
                    this.oldY = -1;
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

        if (this.oldX == -1 && this.oldY == -1) {
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
                this.oldX = -1;
                this.oldY = -1;
                this.game.nextPlayer();
            } else {
                this.oldX = -1;
                this.oldY = -1;
                this.out.println("CLEAR");
                this.processMoveCommand(chosenX, chosenY);
            }

        }
    }

    private Change[] getPossibleMoves(int chosenX, int chosenY) {
        //TODO : get all possible moves
        return (new MoveOneRule()).getPossibleMoves(this.game.getGameContext().getBoard().getCell(chosenX, chosenY), this.game.getGameContext().getBoard());
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
            this.game.resend("LOST", playerId);
            this.out.println("WON");
        }
    }

    public void send(String message) {
        this.out.println(message);
    }

//    else if (command.startsWith("CURRENT")) {
//

//
//            if (isActive(this)) {
//
//            if (isPawnOwner(chosenX, chosenY, this.playerId)) {
//            oldX = chosenX;
//            oldY = chosenY;
//            possibleMoves = createRulesTable(oldX, oldY, this.playerId);
//            String info = infoToString(possibleMoves);
//            out.println("INFO " + info);
//
//            } else {
//            out.println("MESSAGE Choose your pawn!");
//            }
//
//            } else {
//            out.println("MESSAGE Not your turn");
//            }

//    private void processMoveCommand() {
//
//        Cell currentCell = new Cell(oldX, oldY, playerId);
//
//        Iterator gameRulesIterator = new GameRulesIterator(gameContext.getRules());
//        {
//            while (gameRulesIterator.hasNext()) {
//                GameRule gameRule = (GameRule) gameRulesIterator.next();
//                Iterator iterator = new RulesIterator(gameRule.getPossibleMoves(currentCell, gameContext.getBoard()));
//                while (iterator.hasNext()) {
//                    Change c = (Change) iterator.next();
//                    if (c.getX() == newX && c.getY() == newY) {
//                        gameContext.getBoard().getCell(newX, newY).setCellState(currentPlayer.playerId);
//                        gameContext.getBoard().getCell(oldX, oldY).setCellState(0);
//                        for (Player player : players) {
//                            player.out.println("PLAYER_MOVED " + newX + ";" + newY + ";" + currentPlayer.playerId);
//                        }
//                        sendResults(this);
//                        break;
//                    }
//                }
//            }
//        }
//
//    }

//
//    Change[] createRulesTable(int x, int y, int state) {
//        Cell currentCell = new Cell(x,y,state);
//        ArrayList<Change> moves = new ArrayList<Change>();
//        Iterator gameRulesIterator = new GameRulesIterator(gameContext.getRules());
//        while (gameRulesIterator.hasNext()) {
//            GameRule gameRule = (GameRule) gameRulesIterator.next();
//            Iterator iterator = new RulesIterator(gameRule.getPossibleMoves(currentCell, gameContext.getBoard()));
//            while (iterator.hasNext()) {
//                Change change = (Change) iterator.next();
//                moves.add(change);
//            }
//        }
//        return moves.toArray(new Change[0]);
//    }

}
