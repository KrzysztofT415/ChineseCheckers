
import boards.*;
import games.GameContext;
import games.StandardGameContext;
import rules.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.*;

class Game {

    public static List<Player> players = new ArrayList<Player>();
    private int playersNumber;
    int rnd;
    Player currentPlayer;
    GameContext gameContext = new StandardGameContext();
    int[][] possibleMoves;

    public Game(int playersNumber) {
        this.playersNumber = playersNumber;
        rnd = (new Random().nextInt(playersNumber)) + 1;
    }

    /*public synchronized void move(int location, Player player) {
        if (player != currentPlayer) {
            throw new IllegalStateException("Not your turn");
        }
    }*/

    class Player implements Runnable{
        int playerId;

        Socket socket;
        Scanner input;
        PrintWriter output;
        int oldX, oldY, newX, newY;


        public Player(Socket socket, int playerId) {
            this.socket = socket;
            this.playerId = playerId;
        }

        @Override
        public void run() {
            try {
                setup();
                selectFirstPlayer();
                processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }

        private void setup() throws IOException {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            players.add(this);
            if (players.size() == playersNumber) {
                for (Player player : players) {
                    player.output.println("START");
                }
            }

                        //String gameInfo = "";
                        //TODO:
                        // board.asGameInfo =>  length;x1;y1;state1;x2;y2;state2;x3...
                        //player.output.println("START " + gameInfo);

        }



        private void selectFirstPlayer() {
           /*for (int i = 0; i < players.size(); i++) {
                if (i + 1 == rnd) {
                    Player playerOne = players.get(i);
                    players.remove(i);
                    List<Player> temp = new ArrayList<Player>();
                    temp.add(playerOne);
                    playerOne.playerId = 1;
                    for (int j = 0; j < players.size(); j++) {
                        temp.add(players.get(j));
                        temp.get(j + 1).playerId = j + 2;
                    }
                    players = temp;
                    temp = null;*/
            for (Player player : players) {
                System.out.println(player.playerId);
                if (player.playerId == rnd)
                {
                    currentPlayer = this;
                    for (Player player1 : players) {
                        player1.output.println("MESSAGE It's player " + currentPlayer.playerId + " turn!");
                    }
                }
            }



                    //break;
                //}
            //}

        }

        private void processCommands() {
            while (input.hasNextLine()) {
                var command = input.nextLine();
                if (command.startsWith("MOVE")) {
                    String[] temp = command.substring(5).split(";");

                    newX = Integer.parseInt(temp[0]);
                    newY = Integer.parseInt(temp[1]);

                    if (isActive(this)) {
                        processMoveCommand();
                        nextPlayer();
                    } else {
                        output.println("MESSEGE Not your turn");
                    }
                } else if (command.startsWith("CURRENT")) {
                    String[] temp = command.substring(7).split(";");
                    int chosenX = Integer.parseInt(temp[0]);
                    int chosenY = Integer.parseInt(temp[1]);

                    if (isActive(this)) {

                        if (isPawnOwner(chosenX, chosenY, this.playerId)) {
                            oldX = chosenX;
                            oldY = chosenY;
                            Change[] tab = createRulesTable();
                            String info = infoToString(tab);
                            output.println("INFO " + info);

                        } else {
                            output.println("MESSAGE Choose your pawn!");
                        }

                    } else {
                        output.println("MESSAGE Not your turn");
                    }
                } else if (command.startsWith("PASS")) {
                    if (isActive(this)) {
                        nextPlayer();
                    } else {
                        output.println("MESSAGE Not your turn");
                    }
                }
            }
        }

        private void nextPlayer() {
            if (this.playerId == players.size()) {
                currentPlayer = players.get(0);
            } else {
                currentPlayer = players.get(this.playerId);
            }
            for (Player player : players) {
                player.output.println("MESSAGE It's player's " + currentPlayer.playerId + " turn!");

            }

        }

        private void processMoveCommand() {

            Cell currentCell = new Cell(oldX, oldY, playerId);

            Iterator iterator = new RulesIterator(); //
            while (iterator.hasNext()) {
                Change c = (Change) iterator.next();
                if (c.getX() == newX && c.getY() == newY) {
                    gameContext.getBoard().getCell(newX, newY).setCellState(currentPlayer.playerId);
                    gameContext.getBoard().getCell(oldX, oldY).setCellState(0);
                    for (Player player : players) {
                        player.output.println("PLAYER_MOVED " + newX + ";" + newY + ";" + currentPlayer.playerId);
                    }
                    sendResults(this);
                    break;
                }
            }

        }

        void sendResults(Player player) {
            if (gameContext.getBoard().hasWinner() == player.playerId) {
                for (Player player1 : players) {
                    if (player1.playerId != player.playerId) {
                        player.output.println("LOST");
                    }
                    else {
                        player.output.println("WON");
                    }
                }
            }
        }

        boolean isActive(Player player) {
            if (player == currentPlayer) {
                return true;
            }
            else {
                return false;
            }
        }

        boolean isPawnOwner (int x, int y, int id) {
            if (gameContext.getBoard().getCellState(x, y) == id) {
                return true;
            } else {
                return false;
            }
        }

        String infoToString(Change[] info) {
            String result = Integer.toString(info.length);
            for (int i = 1; i <= info.length; ) {
                result = result.concat(";" + Integer.toString(info[i].getX()));
                result = result.concat(";" + Integer.toString(info[i].getY()));
                result = result.concat(";" + Integer.toString(info[i].getState()));
            }
            return result;
        }

        Change[] createRulesTable() {
            GameRule[] gameRule = gameContext.getRules();
            //
        }
    }

}