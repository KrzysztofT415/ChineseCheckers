import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class MainServer {

    public static void main(String[] args) throws Exception {
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
