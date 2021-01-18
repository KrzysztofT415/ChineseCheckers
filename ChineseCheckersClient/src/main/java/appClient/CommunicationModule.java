package appClient;

public interface CommunicationModule {
    /**
     * Method responsible for handling received messages
     * @param response Received String to handle
     * @return application execution value
     */
    int execute(String response);

    /**
     * Method converts provided String to an array containing
     * coordinates x,y and states representing places on board
     * @param response String in a form of [length;x;y;state;x1;y1;state;...]
     * @return Array containing information about places on board
     */
    default int[][] resolveParameters(String response) {
        String[] stringParameters = response.split(";");

        int[][] changes = new int[Integer.parseInt(stringParameters[0])][3];
        for (int i = 0; i < changes.length; i++) {
            changes[i] = new int[]
                    { Integer.parseInt(stringParameters[i * 3 + 1]),
                            Integer.parseInt(stringParameters[i * 3 + 2]),
                            Integer.parseInt(stringParameters[i * 3 + 3]) };
        }

        return changes;
    }
}
