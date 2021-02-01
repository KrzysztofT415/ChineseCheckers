package appServer;

import appServer.connectionDB.GameJDBCTemplate;
import games.GameContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;

/**
 * Class contains and handles information about players
 */
public class Game {


    private GameContext gameContext;

    private GameJDBCTemplate gameJDBCTemplate;
    private int gameId;

    private Player[] players;
    private Player currentPlayer;
    private int numberOfPlayers;

    private int winners = 0;

    public void setGameContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void setGameJDBCTemplate(GameJDBCTemplate gameJDBCTemplate) {
        this.gameJDBCTemplate = gameJDBCTemplate;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void handlePlayers() {
        this.players = new Player[numberOfPlayers];
        gameContext.getBoard().placePlayers(numberOfPlayers);
    }

    /**
     * Method adds new player to the array of players
     * @param newPlayer player to add
     * @param i index in array
     */
    public void connectPlayer(Player newPlayer, int i) {
        this.players[i] = newPlayer;
    }

    /**
     * Method changes current player to the next one in the array of connected players
     */
    public void nextPlayer() {
        int nextPlayerId = this.currentPlayer.getPlayerId() % players.length + 1;
        while (this.gameContext.getBoard().isWinner(nextPlayerId)) {
            nextPlayerId = nextPlayerId % players.length + 1;

            if (nextPlayerId == currentPlayer.getPlayerId() && this.gameContext.getBoard().isWinner(currentPlayer.getPlayerId())) {
                this.resend("END", currentPlayer.getPlayerId());
                this.currentPlayer.getCommunicationService().send("END");
                System.exit(0);
            }
        }
        this.currentPlayer = players[nextPlayerId - 1];
        this.currentPlayer.getCommunicationService().send("MESSAGE It's your turn now!");
    }

    /**
     * Method increments variable winners
     * @return incremented variable winners
     */
    public int getWinners() {
        winners++;
        return winners;
    }

    /**
     * Method to get GameContext of the game
     * @return GameContext of the game
     */
    public GameContext getGameContext() {
        return gameContext;
    }

    /**
     * Method to get id of the current player
     * @return Id if the current player
     */
    public int getCurrentPlayerId() {
        return currentPlayer.getPlayerId();
    }

    /**
     * Method sends some message to every player except the one specified by provided id, if it's
     * impossible shuts down the server
     * @param message Text to be send
     * @param playerWhoSent id of the player who send said message
     */
    public void resend(String message, int playerWhoSent) {
        try {
            for (Player player : players) {
                if (player.getPlayerId() != playerWhoSent) {
                    player.getCommunicationService().send(message);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Somebody is missing, server shutting down");
            System.exit(1);
        }
    }

    /**
     * Method chooses random player as a first player, then sends to client message
     * with information about starting board
     */
    public void play() {
        int randomPlayerId = (new Random().nextInt(players.length));
        this.currentPlayer = players[randomPlayerId];

        int[][] gameInfo = gameContext.getBoard().asGameInfo();
        StringBuilder gameInfoString = new StringBuilder("START " + gameInfo.length);
        for (int[] cellInfo : gameInfo) {
            for (int x : cellInfo) {
                gameInfoString.append(";").append(x);
            }
            this.gameJDBCTemplate.save(this.getGameId(), -cellInfo[2], 0, 0, cellInfo[0], cellInfo[1]);
        }
        System.out.println("Sending starting string to clients - " + gameInfoString);
        for (Player player : players) {
            player.getCommunicationService().send(gameInfoString.toString());
        }

        this.resend("MESSAGE Player "+(randomPlayerId + 1)+" was chosen as first player", randomPlayerId + 1);
        this.currentPlayer.getCommunicationService().send("MESSAGE It's your turn now!");
    }

    public int getGameId() {
        return gameId;
    }

}