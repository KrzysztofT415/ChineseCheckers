package appServer;

import java.net.Socket;

public class Spectator implements User {

    private final ServerCommunicationService communicationService;

    public Spectator(Socket socket) {
        this.communicationService = new ServerCommunicationService(1, socket);
        this.communicationService.connectModule(new WatchingCommunicationModule(communicationService));
    }

    @Override
    public void run() {
        this.communicationService.send("WELCOME 1");
        this.communicationService.start();
        System.exit(1);
    }

    @Override
    public ServerCommunicationService getCommunicationService() {
        return this.communicationService;
    }
}
