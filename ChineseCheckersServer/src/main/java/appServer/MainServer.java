package appServer;

import java.net.InetAddress;
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
            try (ServerSocket listener = new ServerSocket(58901)) {

                System.out.println("Chinese Checkers Server is Running");
                System.out.println("Server ip : " + InetAddress.getLocalHost().getHostAddress() + ":58901");

                ExecutorService pool = Executors.newFixedThreadPool(lobbySize);

                Game game = new Game(lobbySize);
                System.out.println("New game created - lobby size : " + lobbySize);

                for (int i = 1; i <= lobbySize; i++) {
                    System.out.println("Waiting for player " + i);

                    Player newPlayer = new Player(i, game, listener.accept());
                    game.connectPlayer(newPlayer, i - 1);
                    pool.execute(newPlayer);

                    System.out.println("app.Player " + i + " joined");
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

    public static int verifyArguments(String[] args) throws IllegalArgumentException {
        if (args.length != 1) { throw new IllegalArgumentException("Incorrect number of arguments"); }

        int numberOfPlayers;
        try { numberOfPlayers = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) { throw new IllegalArgumentException("Incorrect argument format"); }

        if (numberOfPlayers != 2 && numberOfPlayers != 3 && numberOfPlayers != 4 && numberOfPlayers != 6) {
            throw new IllegalArgumentException("Incorrect number of players");
        }

        return numberOfPlayers;
    }

    public static void main(String[] args) {
        int numberOfPlayers;
        try { numberOfPlayers = MainServer.verifyArguments(args); }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        new MainServer(numberOfPlayers).run();
    }
}
