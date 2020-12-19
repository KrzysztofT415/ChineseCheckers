import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer implements Runnable {

    private final int lobbySize;

    public MainServer(int lobbySize) {
        this.lobbySize = lobbySize;
    }

    @Override
    public void run() {
        try {
            if (lobbySize != 2 && lobbySize != 3 && lobbySize != 4 && lobbySize != 6) {
                System.out.println("Wrong number of players");
                return;
            }

            try (ServerSocket listener = new ServerSocket(58901)) {

                System.out.println("Chinese Checkers Server is Running");

                ExecutorService pool = Executors.newFixedThreadPool(lobbySize);

                Game game = new Game(lobbySize);
                System.out.println("New game created - lobby size : " + lobbySize);

                for (int i = 1; i <= lobbySize; i++) {
                    System.out.println("Waiting for player " + i);

                    Player newPlayer = new Player(i, game, listener.accept());
                    game.connectPlayer(newPlayer, i - 1);
                    pool.execute(newPlayer);

                    System.out.println("Player " + i + " joined");
                }
                synchronized (this) {
                    wait(100);
                }
                game.play();
                System.out.println("All players are in lobby - game starting");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Specify number of players");
            return;
        }

        int numberOfPlayers;
        try {
            numberOfPlayers = Integer.parseInt(args[0]);
            new MainServer(numberOfPlayers).run();
        } catch (NumberFormatException e) {
            System.out.println("Wrong argument format");
        }
    }
}
