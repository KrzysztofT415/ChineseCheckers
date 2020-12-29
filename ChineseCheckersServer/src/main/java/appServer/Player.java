package appServer;

import java.net.Socket;

class Player implements Runnable {

    private final int playerId;
    private final Game game;
    private final ServerCommunicationService communicationService;

    public Player(int playerId, Game game, Socket socket) {
        this.playerId = playerId;
        this.game = game;
        this.communicationService = new ServerCommunicationService(playerId, game, socket);
    }

    @Override
    public void run() {
        this.communicationService.send("WELCOME "+this.playerId);
        this.communicationService.start();
        this.game.resend("LEAVE", playerId);
        System.exit(1);
    }

    public int getPlayerId() {
        return playerId;
    }

    public ServerCommunicationService getCommunicationService() {
        return communicationService;
    }
}