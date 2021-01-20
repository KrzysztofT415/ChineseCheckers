package appServer;

import appServer.connectionDB.GameJDBCTemplate;

import java.net.Socket;

/**
 * Class representing a player that connects to the server
 */
class Player implements User {

    private final int playerId;
    private final Game game;
    private final ServerCommunicationService communicationService;

    public Player(int playerId, Game game, GameJDBCTemplate gameJDBCTemplate, Socket socket) {
        this.playerId = playerId;
        this.game = game;
        this.communicationService = new ServerCommunicationService(playerId, socket);
        this.communicationService.connectModule(new GameCommunicationModule(playerId, game, gameJDBCTemplate, communicationService));
    }

    /**
     * Method invokes method responsible for communication with client.
     * When player leaves the game it invokes the method that sends information about it to other
     * players and then shuts down
     */
    @Override
    public void run() {
        this.communicationService.send("WELCOME "+this.playerId);
        this.communicationService.start();
        this.game.resend("LEAVE", playerId);
        System.exit(1);
    }

    /**
     * Method to get the id of the player
     * @return Id of this player
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Method to get the ServerCommunicationService of the player
     * @return ServerCommunicationService of this player
     */
    public ServerCommunicationService getCommunicationService() {
        return communicationService;
    }
}