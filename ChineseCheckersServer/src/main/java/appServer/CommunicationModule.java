package appServer;
/**
 * Interface for classes responsible for handling messages from the clients
 */
public interface CommunicationModule {
    /**
     * Method responsible for identifying and handling received commands
     * @param command received command
     * @return application execution value
     */
    int execute(String command);
}