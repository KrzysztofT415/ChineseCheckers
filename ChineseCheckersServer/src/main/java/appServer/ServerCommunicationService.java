package appServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class containing methods responsible for receiving messages from clients
 */
public class ServerCommunicationService {

    private int playerId;

    private Scanner in;
    private PrintWriter out;

    private CommunicationModule communicationModule;

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setSocket(Socket socket) {
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public ServerCommunicationService(int playerId, Socket socket) {
        this.playerId = playerId;

        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Method to set the right CommunicationModule which is responsible for processing messages
     * received from client
     * @param communicationModule CommunicationModule being used for the game
     */
    public void connectModule(CommunicationModule communicationModule) {
        this.communicationModule = communicationModule;
    }

    /**
     * Method checks if some message from client was received, reads it and
     * invokes a method responsible for processing said message
     */
    public void start() {
        while (this.in.hasNextLine()) {
            String command = in.nextLine();
            System.out.println(playerId + " : " + command);

            int result = this.communicationModule.execute(command);

            if (result == 1) { break; }
        }
    }

    /**
     * Method sends passed String to the client
     * @param message Text to be sent
     */
    public void send(String message) {
        this.out.println(message);
    }

}
