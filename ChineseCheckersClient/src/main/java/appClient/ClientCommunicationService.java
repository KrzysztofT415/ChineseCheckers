package appClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class containing methods responsible for reading and sending messages from server
 */
public class ClientCommunicationService {

    private final MainClient app;
    private int playerId;

    private Scanner in;
    private PrintWriter out;

    private CommunicationModule communicationModule;

    /**
     * Creates Scanner and PrintWriter for client to receive and send messages from and to the server
     * @param app MainClient for which these objects are created
     * @param socket Socket to which client is connected
     */
    public ClientCommunicationService(MainClient app, Socket socket) {
        this.app = app;

        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectModule(CommunicationModule communicationModule) {
        this.communicationModule = communicationModule;
    }

    /**
     * Method checks if some message from server was received, reads it and
     * invokes a method responsible for processing said message
     */
    public void start() {
        System.out.println("\n---\nCLIENT COMMUNICATION SERVICE :");
        String response = in.nextLine();
        System.out.println(response);
        this.playerId = Integer.parseInt(response.substring(8));
        this.app.getAppWindow().setPlayer(playerId);

        while (in.hasNextLine()) {
            response = in.nextLine();
            System.out.println(response);

            int result = this.communicationModule.execute(response);

            if (result == 1) { break; }
        }
        this.out.println("QUIT");
    }

    /**
     * Method sends provided String to the server via PrintWriter
     * @param message Text to be sent
     */
    public void send(String message) {
        this.out.println(message);
    }

}
