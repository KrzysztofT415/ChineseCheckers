package appServer;

import appServer.connectionDB.GameJDBCTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.net.DatagramSocket;
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

    private int gameId;
    private int lobbySize;

    private final ApplicationContext applicationContext;
    private final GameJDBCTemplate gameJDBCTemplate;

    /**
     * Acts as lobby for players. Waits until all players are connected.
     * After all players joined sends information about game starting to all clients.
     */
    public MainServer(int lobbySize) {
        this.lobbySize = lobbySize;
        this.applicationContext = new ClassPathXmlApplicationContext("Beans.xml");
        this.gameJDBCTemplate = (GameJDBCTemplate) applicationContext.getBean("gameJDBCTemplate");
    }

    public MainServer(int lobbySize, int gameId) {
        this(lobbySize);
        this.gameId = gameId;
    }


    /**
     * Runs the lobby on localhost and random port.
     * Prints ip to which players should connect.
     * Also prints information about current connecting-events into terminal.
     */
    @Override
    public void run() {
        try {
            int port = 1313;

            String ip;
            try(final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
                System.out.println("SERVER IP : " + ip + ":" + port);
            }

            try (ServerSocket listener = new ServerSocket(port, 0, InetAddress.getByName(ip))) {

                System.out.println("Chinese Checkers Server is Running");

                ExecutorService pool = Executors.newFixedThreadPool(lobbySize);

                if (lobbySize == 1) {
                    System.out.println("Waiting for player who want to watch game to connect");
                    Spectator spectator = new Spectator(gameId, listener.accept());
                    pool.execute(spectator);
                    System.out.println("Player connected - started displaying game");

                    synchronized (this) { wait(100); }
                    spectator.sendStartingBoard();

                } else {
                    gameJDBCTemplate.addNewGame(ip);
                    this.gameId = gameJDBCTemplate.getNewGameId(ip).getGameId();
                        Game game = (Game) applicationContext.getBean("game");
                        game.setNumberOfPlayers(lobbySize);
                        game.setGameId(gameId);
                        game.handlePlayers();

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

        int numberOfPlayers;
        try { numberOfPlayers = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) { throw new IllegalArgumentException("Incorrect argument format"); }

        if (numberOfPlayers != 1 && numberOfPlayers != 2 && numberOfPlayers != 3 && numberOfPlayers != 4 && numberOfPlayers != 6) {
            throw new IllegalArgumentException("Incorrect number of players");
        }

        if (numberOfPlayers == 1) {
            if (args[1] == null) { throw new IllegalArgumentException("Incorrect args"); }

            try { Integer.parseInt(args[0]);
            } catch (NumberFormatException e) { throw new IllegalArgumentException("Incorrect gameId format"); }

        }

        return numberOfPlayers;
    }

    /**
     * Runs the application. Verifies arguments and then creates lobby for expected players.
     */
    public static void main(String[] args) {
        int numberOfPlayers;
        String nm = JOptionPane.showInputDialog("Choose number of players (1,2,3,4,6) :");
        String id = null;
        if (nm == null) { System.exit(1); }
        if (nm.equals("1")) {
            id = JOptionPane.showInputDialog("Choose id of game :");
        }

        args = new String[] {nm, id};

        try { numberOfPlayers = MainServer.verifyArguments(args); }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (args[1] != null) { new MainServer(numberOfPlayers, Integer.parseInt(args[1])).run(); }
        new MainServer(numberOfPlayers).run();
    }

}
