package app;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientCommunicationService {

    private final MainClient app;
    private int playerId;

    private Scanner in;
    private PrintWriter out;

    public ClientCommunicationService(MainClient app, Socket socket) {
        this.app = app;

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
        String response = in.nextLine();
        System.out.println(response);
        this.playerId = Integer.parseInt(response.substring(8));
        this.app.getAppWindow().setPlayer(playerId);

        while (in.hasNextLine()) {
            response = in.nextLine();
            System.out.println(response);

            if (response.startsWith("START")) {
                int[][] gameInfo = this.resolveParameters(response.substring(6));

                this.app.getAppWindow().setStartingBoard(gameInfo);
            }

            else if (response.startsWith("SET")) {
                int[][] gameInfo = this.resolveParameters(response.substring(4));

                for (int[] change : gameInfo) {
                    this.app.getAppWindow().getBoardView().updateCellState(change[0], change[1], change[2]);
                }
            }

            else if (response.startsWith("CLEAR")) {
                this.app.getAppWindow().getBoardView().clearPossibleMoves();
            }

            else if (response.startsWith("MESSAGE")) {
                this.app.getAppWindow().setMessage(response.substring(8));
            }

            else if (response.startsWith("LOST")) {
                JOptionPane.showMessageDialog(null, "Somebody won");
            }

            else if (response.startsWith("WON")) {
                JOptionPane.showMessageDialog(null, "You won " + response.substring(4) + " place!");
            }

            else if (response.startsWith("LEAVE")) {
                JOptionPane.showMessageDialog(null, "Someone left the game");
                break;
            }

            else if (response.startsWith("END")) {
                JOptionPane.showMessageDialog(null, "Game Ended!");
                break;
            }
        }
        this.out.println("QUIT");
    }

    public void sendClick(int x, int y) {
        this.out.println("CLICK "+x+";"+y);
    }

    public void send(String message) {
        this.out.println(message);
    }

}
