package appServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerCommunicationService {

    private final int playerId;

    private Scanner in;
    private PrintWriter out;

    private CommunicationModule communicationModule;

    public ServerCommunicationService(int playerId, Socket socket) {
        this.playerId = playerId;

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

    public void start() {
        while (this.in.hasNextLine()) {
            String command = in.nextLine();
            System.out.println(playerId + " : " + command);

            int result = this.communicationModule.execute(command);

            if (result == 1) { break; }
        }
    }

    public void send(String message) {
        this.out.println(message);
    }

}
