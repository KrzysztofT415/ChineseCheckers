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

    public Game(int playersNumber) {
        this.playersNumber = playersNumber;
        rnd = (new Random().nextInt(playersNumber)) - 1;
    }

    class Player implements Runnable {
        int playerId;

        Socket socket;
        Scanner input;
        PrintWriter output;

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
                        String gameInfo = "";
                        //TODO:
                        // board.asGameInfo =>  length;x1;y1;state1;x2;y2;state2;x3...
                        player.output.println("START " + gameInfo);
                    }
                    break;
                }
            }
        }

        private void selectFirstPlayer () {
            for (int i = 0; i<players.size(); i++) {
                if(i == rnd)
                {
                    currentPlayer = this;
                }
                players.get(i).output.println("PLAYER It's player's " + rnd + "turn!");
            }
        }

        private void processCommands() {
            while (input.hasNextLine()) {
                var command = input.nextLine();
                if (command.startsWith("MOVE")) {
                    return;
                }
            }
        }
    }
}
