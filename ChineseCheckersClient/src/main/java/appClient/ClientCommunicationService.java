package appClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientCommunicationService {

    private final MainClient app;
    private int playerId;

    private Scanner in;
    private PrintWriter out;

    private CommunicationModule communicationModule;

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

    public void send(String message) {
        this.out.println(message);
    }

}
