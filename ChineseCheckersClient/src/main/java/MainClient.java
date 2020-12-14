
import view.BoardView;

import javax.swing.*;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

public class MainClient {

    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;

    private final JFrame frame = new JFrame("Chinese Checkers");
    private BoardView boardView;

    public MainClient() throws Exception {

        socket = new Socket(InetAddress.getLocalHost(), 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    public void play() throws Exception {
        try {
            while (in.hasNextLine()) {
                var response = in.nextLine();
                if (response.startsWith("START")) {

                    response = response.substring(6);
                    String[] stringParameters = response.split(";");
                    int[][] gameInfo = new int[Integer.parseInt(stringParameters[0])][3];
                    for (int i = 0; i < gameInfo.length; i++) {
                        gameInfo[i] = new int[]
                                { Integer.parseInt(stringParameters[i * 3 + 1]),
                                  Integer.parseInt(stringParameters[i * 3 + 2]),
                                  Integer.parseInt(stringParameters[i * 3 + 3]) };
                    }

                    this.boardView = new BoardView(gameInfo);
                    this.frame.add(this.boardView);

                    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    this.frame.setSize(320, 320);
                    this.frame.setVisible(true);
                    this.frame.setResizable(false);

                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
//
//    public static void main(String[] args) throws Exception {
//        MainClient client = new MainClient();
//        client.play();
//    }

    // Main to test showing board (uses gameInfo pasted from server)
    public static void main(String[] args) {

        BoardView boardView = new BoardView(new int[][] {{-4,4,0},{-4,3,0},{-4,2,0},{-4,1,0},{-4,0,0},{-3,4,0},{-3,3,0},{-3,2,0},{-3,1,0},{-3,0,0},{-3,-1,0},{-2,4,0},{-2,3,0},{-2,2,0},{-2,1,0},{-2,0,0},{-2,-1,0},{-2,-2,0},{-1,4,0},{-1,3,0},{-1,2,0},{-1,1,0},{-1,0,0},{-1,-1,0},{-1,-2,0},{-1,-3,0},{0,4,0},{0,3,0},{0,2,0},{0,1,0},{0,0,0},{0,-1,0},{0,-2,0},{0,-3,0},{0,-4,0},{1,3,0},{1,2,0},{1,1,0},{1,0,0},{1,-1,0},{1,-2,0},{1,-3,0},{1,-4,0},{2,2,0},{2,1,0},{2,0,0},{2,-1,0},{2,-2,0},{2,-3,0},{2,-4,0},{3,1,0},{3,0,0},{3,-1,0},{3,-2,0},{3,-3,0},{3,-4,0},{4,0,0},{4,-1,0},{4,-2,0},{4,-3,0},{4,-4,0},{1,-5,1},{2,-5,1},{3,-5,1},{4,-5,1},{2,-6,1},{3,-6,1},{4,-6,1},{3,-7,1},{4,-7,1},{4,-8,1},{-4,-1,2},{-3,-2,2},{-2,-3,2},{-1,-4,2},{-4,-2,2},{-3,-3,2},{-2,-4,2},{-4,-3,2},{-3,-4,2},{-4,-4,2},{-5,4,3},{-5,3,3},{-5,2,3},{-5,1,3},{-6,4,3},{-6,3,3},{-6,2,3},{-7,4,3},{-7,3,3},{-8,4,3},{-1,5,4},{-2,5,4},{-3,5,4},{-4,5,4},{-2,6,4},{-3,6,4},{-4,6,4},{-3,7,4},{-4,7,4},{-4,8,4},{4,1,5},{3,2,5},{2,3,5},{1,4,5},{4,2,5},{3,3,5},{2,4,5},{4,3,5},{3,4,5},{4,4,5},{5,-4,6},{5,-3,6},{5,-2,6},{5,-1,6},{6,-4,6},{6,-3,6},{6,-2,6},{7,-4,6},{7,-3,6},{8,-4,6}} );
//        BoardView boardView = new BoardView(new int[][]{{0, 1, 1}, {-1,1,2}, {-1,0,3}, {0,-1,4}, {1,-1,5}, {1,0,6}});
        JFrame frame = new JFrame();
        frame.add(boardView);
        frame.setSize(BoardView.WINDOW_WIDTH, BoardView.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}