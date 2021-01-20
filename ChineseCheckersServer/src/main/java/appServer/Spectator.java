package appServer;

import appServer.connectionDB.GameJDBCTemplate;

import java.net.Socket;

public class Spectator implements User {

    private final ServerCommunicationService communicationService;
    private final WatchingCommunicationModule watchingCommunicationModule;

    public Spectator(int gameId, GameJDBCTemplate gameJDBCTemplate, Socket socket) {
        this.communicationService = new ServerCommunicationService(1, socket);
        this.watchingCommunicationModule = new WatchingCommunicationModule(gameId, gameJDBCTemplate, communicationService);
        this.communicationService.connectModule(watchingCommunicationModule);
    }

    @Override
    public void run() {
        this.communicationService.send("WELCOME 3");
        this.communicationService.start();
        System.exit(1);
    }

    @Override
    public ServerCommunicationService getCommunicationService() {
        return this.communicationService;
    }

    public void sendStartingBoard() {
        this.communicationService.send(watchingCommunicationModule.getStartingBoard().toString());
    }
}
