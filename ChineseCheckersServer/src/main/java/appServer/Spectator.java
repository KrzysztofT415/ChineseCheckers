package appServer;

import appServer.connectionDB.GameJDBCTemplate;

import java.net.Socket;

public class Spectator implements User {

    private ServerCommunicationService communicationService;
    private WatchingCommunicationModule watchingCommunicationModule;


    public Spectator(int gameId, Socket socket) {
        //this.communicationService = new ServerCommunicationService(1, socket);

        this.communicationService = new ServerCommunicationService();
        this.communicationService.setPlayerId(1);
        this.communicationService.setSocket(socket);


        //this.watchingCommunicationModule = new WatchingCommunicationModule(gameId, communicationService);

        this.watchingCommunicationModule = new WatchingCommunicationModule(gameId, communicationService);
        //this.watchingCommunicationModule.setGameId(gameId);
        //this.watchingCommunicationModule.setCommunicationService(communicationService);
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
