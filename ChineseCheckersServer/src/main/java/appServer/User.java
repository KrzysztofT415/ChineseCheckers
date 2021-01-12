package appServer;

public interface User extends Runnable {
    ServerCommunicationService getCommunicationService();
}
