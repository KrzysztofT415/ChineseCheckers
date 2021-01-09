package appClient;

public interface CommunicationModule {
    /**
     * Method responsible for handling received messages
     * @param response Received String to handle
     * @return application work value
     */
    int execute(String response);
}
