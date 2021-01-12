package appServer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server for a network multi-player Chinese Checkers game.
 *
 * !important! While compiling pass as argument number of players that are expected to be in game
 *             correct number of players are : 2, 3, 4, 6
 *
 * Communication between server and clients is based on plain text, called as ChChP (Chinese Checkers Protocol)
 * The strings that are sent in ChChP are:
 *
 *----------------
 * Client -> Server
 *
 * CLICK [x;y]
 * PASS
 * QUIT
 *
 * where:
 *  * in CLICK [x;y] are coordinates of clicked cell
 * ----------------
 * Server -> Client
 *
 * WELCOME [n] (1 <= n <= 6)
 * START [n;x1;y1;s1;x2;y2...]
 * SET [n;x1;y1;s1;x2;y2...]
 * CLEAR
 * MESSAGE [String]
 * LOST
 * WON [n] (1 <= n <= 6)
 * LEAVE
 * END
 *
 * where:
 * in SET and START [n;x1;y1;s1;x2;y2...]:
 *   <n> means number of changed cells
 *   x[i], y[i] are coordinates of changed cell
 *   s[i] is new state of this cell
 *   where 0 < [i] <= <n>
 * in WELCOME [n] is playerId of player that joined
 * in WON [n] is playerId of player that won game
 * in MESSAGE [String] is text to be displayed in message box on client
 * ----------------
 *
 * @author Martyna Dziamara, Krzysztof Talalaj
 *
 */
public class MainServer implements Runnable {

    private final int lobbySize;

    /**
     * Acts as lobby for players. Waits until all players are connected.
     * After all players joined sends information about game starting to all clients.
     * @param lobbySize number of players to join
     */
    public MainServer(int lobbySize) {
        this.lobbySize = lobbySize;
    }

    /**
     * Runs the lobby on localhost and random port.
     * Prints ip to which players should connect.
     * Also prints information about current connecting-events into terminal.
     */
    @Override
    public void run() {
        try {
            int port = new Random().nextInt(10000);
            try (ServerSocket listener = new ServerSocket(port)) {

                System.out.println("Chinese Checkers Server is Running");
                System.out.println("Server ip : " + InetAddress.getLocalHost().getHostAddress() + ":" + port);

                ExecutorService pool = Executors.newFixedThreadPool(lobbySize);

                if (lobbySize == 1) {
                    System.out.println("Waiting for player who want to watch game to connect");
                    Spectator spectator = new Spectator(listener.accept());
                    pool.execute(spectator);
                    System.out.println("Player connected - started displaying game");

                    synchronized (this) { wait(100); }
                    spectator.getCommunicationService().send("START 4;0;0;1;0;1;0;1;0;0;1;1;0");

                } else {
                    Game game = new Game(lobbySize);
                    System.out.println("New game created - lobby size : " + lobbySize);

                    for (int i = 1; i <= lobbySize; i++) {
                        System.out.println("Waiting for player " + i);

                        Player newPlayer = new Player(i, game, listener.accept());
                        game.connectPlayer(newPlayer, i - 1);
                        pool.execute(newPlayer);

                        System.out.println("Player " + i + " joined");
                    }
                    synchronized (this) { wait(100); }
                    game.play();
                    System.out.println("All players are in lobby - game starting");
                }

                System.out.println("\n---\nSERVER COMMUNICATION SERVICE :");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifies arguments.
     * @param args arguments to be verified
     * @return number of players
     * @throws IllegalArgumentException when arguments are incorrect
     */
    public static int verifyArguments(String[] args) throws IllegalArgumentException {
        if (args.length != 1) { throw new IllegalArgumentException("Incorrect number of arguments"); }

        int numberOfPlayers;
        try { numberOfPlayers = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) { throw new IllegalArgumentException("Incorrect argument format"); }

        if (numberOfPlayers != 1 && numberOfPlayers != 2 && numberOfPlayers != 3 && numberOfPlayers != 4 && numberOfPlayers != 6) {
            throw new IllegalArgumentException("Incorrect number of players");
        }

        return numberOfPlayers;
    }

    /**
     * Runs the application. Verifies arguments and then creates lobby for expected players.
     */
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
