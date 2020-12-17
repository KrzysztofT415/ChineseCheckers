import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientCommunicationService {

    private final MainClient mainClient;

    private Scanner in;
    private PrintWriter out;

    public ClientCommunicationService(MainClient mainClient, Socket socket) {
        this.mainClient = mainClient;

        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[][] resolveParameters(String response) {
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

    public void start() {
        while (in.hasNextLine()) {
            String response = in.nextLine();

            if (response.startsWith("START")) {
                int[][] gameInfo = this.resolveParameters(response.substring(6));
                
                mainClient.setStartingBoard(gameInfo);
            }

            else if (response.startsWith("SET")) {
                int[][] gameInfo = this.resolveParameters(response.substring(4));

                for (int[] change : gameInfo) {
                    mainClient.getBoardView().updateCellState(change[0], change[1], change[2]);
                }
            }

            else if (response.startsWith("MESSAGE")) {
                mainClient.setMessageLabel(response.substring(8));
            }

            else if (response.startsWith("LOST")) {
                JOptionPane.showMessageDialog(null, "You lost");
                break;
            }

            else if (response.startsWith("WON")) {
                JOptionPane.showMessageDialog(null, "You won!");
                break;
            }
        }
        out.println("QUIT");
    }

    public void sendClick(int x, int y) {
        out.println("CLICK "+x+";"+y);
    }

    public void send(String message) {
        out.println(message);
    }
}
