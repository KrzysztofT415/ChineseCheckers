import boards.*;
import rules.*;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.*;

class Game {

    private static List<Player> players = new ArrayList<Player>();
    private int playersNumber;
    int rnd;
    Player currentPlayer;
    GameBoard board = new StandardBoard();

    public Game(int playersNumber) {
        this.playersNumber = playersNumber;
        rnd = (new Random().nextInt(playersNumber)) - 1;
    }

    public synchronized void move(int location, Player player) {
        if (player != currentPlayer) {
            throw new IllegalStateException("Not your turn");
        }
    }


    class Player implements Runnable {
        int playerId;

        Socket socket;
        Scanner input;
        PrintWriter output;
        int chosenX, chosenY, newX, newY;

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
            while (true) {
                if (players.size() == playersNumber) {
                    for (Player player : players) {
                        player.output.println("START");
                    }
                    break;
                }
            }
        }

        private void selectFirstPlayer () {
            for (int i = 0; i<players.size(); i++) {
                if(i == rnd)
                {
                    Player playerOne = players.get(i);
                    players.remove(i);
                    List<Player> temp = new ArrayList<Player>();
                    temp.add(playerOne);
                    playerOne.playerId = 1;
                    for (int j = 0; j<players.size(); j++)
                    {
                        temp.add(players.get(j));
                        temp.get(j+1).playerId = j+2;
                    }
                    players = temp;
                    temp = null;
                    currentPlayer = this;
                    break;
                }
            }

        }

        private void processCommands() {
            while (input.hasNextLine()) {
                var command = input.nextLine();
                if (command.startsWith("MOVE")) {
                    String [] temp = command.substring(5).split("\\|");

                    chosenX = Integer.parseInt(temp[0]);
                    chosenY = Integer.parseInt(temp[1]);

                    if (this == currentPlayer) {
                        // if(isMoveAvailable())
                        processMoveCommand(chosenX, chosenY);
                    }
                    else {
                        output.println("MESSEGE Not your turn");
                    }
                }
                else if (command.startsWith("CURRENT")) {
                    String [] temp = command.substring(7).split("\\|");
                    chosenX = Integer.parseInt(temp[0]);
                    chosenY = Integer.parseInt(temp[1]);

                    if (this == currentPlayer) {

                        if (board.getCellState(chosenX, chosenY) == currentPlayer.playerId) {
                            //checkMoves(chosenX, chosenY, playerId);

                        } else {
                            output.println("MESSAGE Choose your pawn!");
                        }

                    }
                    else {
                        output.println("MESSAGE Not your turn");
                    }
                }
                else if (command.startsWith("PASS")) {
                    if (this == currentPlayer) {
                        nextPlayer();
                    }
                    else {
                        output.println("MESSAGE Not your turn");
                    }
                }
            }
        }

        private void nextPlayer() {
            if(this.playerId == players.size() - 1) {
                currentPlayer = players.get(0);
            }
            else {
                currentPlayer = players.get(this.playerId + 1);
            }
        }

        private void processMoveCommand(int x, int y) {


            GameRule rule = new MoveOneRule();
            Cell currentCell = new Cell(x, y, playerId);
            Iterator iterator = new RulesIterator(rule.getPossibleMoves(currentCell, board));
            while(iterator.hasNext()) {
                Change c = (Change)iterator.next();
                if(c.getX() == newX && c.getY() == newY) {
                    board.getCell(chosenX, chosenY).setCellState(currentPlayer.playerId);
                    break;
                }
            }
        }

    }


    }
