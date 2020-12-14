import boards.GameBoard;
import boards.StandardBoard;

import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class MainServer {

    public static void main(String[] args) throws Exception {
//        Test for generating board: (prints gameInfo to paste into client)
//        GameBoard board = new StandardBoard();
//        board.placePlayers(2);
//        int[][] in = board.asGameInfo();
//        for (int[] ints : in) {
//            System.out.printf("{%d,%d,%d},",ints[0],ints[1],ints[2]);
//        }
//        System.out.println();

        if (args.length != 1) {
            System.out.println("Wrong number of arguments");
            return;
        }

        int numberOfPlayers;
        try {
            numberOfPlayers = Integer.parseInt(args[0]);
            if (numberOfPlayers != 2 || numberOfPlayers != 3 || numberOfPlayers != 4 || numberOfPlayers != 6)
            {
                System.out.println("Wrong number of players");
                return;
            }
            try (var listener = new ServerSocket(58901)) {
                System.out.println("Chinese Checkers Server is Running...");

                var pool = Executors.newFixedThreadPool(numberOfPlayers);

                Game game = new Game(numberOfPlayers);
                System.out.println("New game created - lobby size : " + numberOfPlayers);

                for (int i = 1; i <= numberOfPlayers; i++) {
                    System.out.println("Waiting for player " + i);
                    pool.execute(game.new Player(listener.accept(), (char) i));
                    System.out.println("Player " + i + " joined");
                }

                System.out.println("All players are in lobby - game starting");
            }

        } catch (NumberFormatException e) {
            System.out.println("Wrong argument format");
        }
    }

}
