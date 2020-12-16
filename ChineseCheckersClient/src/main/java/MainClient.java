import view.BoardView;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

public class MainClient {

    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;

    //
    private JLabel messageLabel = new JLabel("...");
    //
    private final JFrame frame = new JFrame("Chinese Checkers");
    private BoardView boardView;
    private int[][] moves;





    public MainClient() throws Exception {

        socket = new Socket(InetAddress.getLocalHost(), 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        boardView = new BoardView(new int[][] {{-4,4,0},{-4,3,0},{-4,2,0},{-4,1,0},{-4,0,0},{-3,4,0},{-3,3,0},{-3,2,0},{-3,1,0},{-3,0,0},{-3,-1,0},{-2,4,0},{-2,3,0},{-2,2,0},{-2,1,0},{-2,0,0},{-2,-1,0},{-2,-2,0},{-1,4,0},{-1,3,0},{-1,2,0},{-1,1,0},{-1,0,0},{-1,-1,0},{-1,-2,0},{-1,-3,0},{0,4,0},{0,3,0},{0,2,0},{0,1,0},{0,0,0},{0,-1,0},{0,-2,0},{0,-3,0},{0,-4,0},{1,3,0},{1,2,0},{1,1,0},{1,0,0},{1,-1,0},{1,-2,0},{1,-3,0},{1,-4,0},{2,2,0},{2,1,0},{2,0,0},{2,-1,0},{2,-2,0},{2,-3,0},{2,-4,0},{3,1,0},{3,0,0},{3,-1,0},{3,-2,0},{3,-3,0},{3,-4,0},{4,0,0},{4,-1,0},{4,-2,0},{4,-3,0},{4,-4,0},{1,-5,1},{2,-5,1},{3,-5,1},{4,-5,1},{2,-6,1},{3,-6,1},{4,-6,1},{3,-7,1},{4,-7,1},{4,-8,1},{-4,-1,2},{-3,-2,2},{-2,-3,2},{-1,-4,2},{-4,-2,2},{-3,-3,2},{-2,-4,2},{-4,-3,2},{-3,-4,2},{-4,-4,2},{-5,4,3},{-5,3,3},{-5,2,3},{-5,1,3},{-6,4,3},{-6,3,3},{-6,2,3},{-7,4,3},{-7,3,3},{-8,4,3},{-1,5,4},{-2,5,4},{-3,5,4},{-4,5,4},{-2,6,4},{-3,6,4},{-4,6,4},{-3,7,4},{-4,7,4},{-4,8,4},{4,1,5},{3,2,5},{2,3,5},{1,4,5},{4,2,5},{3,3,5},{2,4,5},{4,3,5},{3,4,5},{4,4,5},{5,-4,6},{5,-3,6},{5,-2,6},{5,-1,6},{6,-4,6},{6,-3,6},{6,-2,6},{7,-4,6},{7,-3,6},{8,-4,6}} );
//        BoardView boardView = new BoardView(new int[][]{{0, 1, 1}, {-1,1,2}, {-1,0,3}, {0,-1,4}, {1,-1,5}, {1,0,6}});
        //JFrame frame = new JFrame();
        frame.add(boardView);
        frame.setSize(BoardView.WINDOW_WIDTH, BoardView.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        //
        frame.getContentPane().add(messageLabel, BorderLayout.SOUTH);

    }

    void updateMove(String string) {
        String [] temp = string.split(";");
        int x = Integer.parseInt(temp[0]);
        int y = Integer.parseInt(temp[1]);
        int state = Integer.parseInt(temp[2]);
        boardView.updateCellState(x, y, state);
    }

    public void showMoves(int[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            tab[i][2] = 7;
            boardView.updateCellState(tab[i][0], tab[i][1], tab[i][2]);
        }
    }

    public void updateAfterMove(int[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            if (tab[i][2] == 7) {
                boardView.updateCellState(tab[i][0], tab[i][1], 0);
            }
        }
    }

    public int[][] createTab(String info) {
        String [] temp = info.split(";");
        int k = 1;
        int length = Integer.parseInt(temp[0]);
        int[][] tab = new int[length][3];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 3; j++)
            {
                tab[i][j] = Integer.parseInt(temp[k]);
                k++;
            }
        }
        return tab;
    }

    public void play() throws Exception {
        try {
            while (in.hasNextLine()) {
                var response = in.nextLine();
                System.out.println(response);
                if (response.startsWith("START")) {
                    /*String[] stringParameters = response.split(";");
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

                    break;*/
                }
                else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                }
                else if (response.startsWith("PLAYER_MOVED")) {
                    String string = response.substring(13);
                    updateMove(string);
                    updateAfterMove(moves);
                }
                else if (response.startsWith("INFO")) {
                    String string = response.substring(5);
                    moves = createTab(string);
                    showMoves(moves);
                }
                else if (response.startsWith("LOST")) {
                    JOptionPane.showMessageDialog(null, "You lost");
                    System.exit(0);
                }
                else if (response.startsWith("WON")) {
                    JOptionPane.showMessageDialog(null, "You won!");
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    //
   public static void main(String[] args) throws Exception {
        MainClient client = new MainClient();
        client.play();
   }

    // Main to test showing board (uses gameInfo pasted from server)
    /*public static void main(String[] args) {

        BoardView boardView = new BoardView(new int[][] {{-4,4,0},{-4,3,0},{-4,2,0},{-4,1,0},{-4,0,0},{-3,4,0},{-3,3,0},{-3,2,0},{-3,1,0},{-3,0,0},{-3,-1,0},{-2,4,0},{-2,3,0},{-2,2,0},{-2,1,0},{-2,0,0},{-2,-1,0},{-2,-2,0},{-1,4,0},{-1,3,0},{-1,2,0},{-1,1,0},{-1,0,0},{-1,-1,0},{-1,-2,0},{-1,-3,0},{0,4,0},{0,3,0},{0,2,0},{0,1,0},{0,0,0},{0,-1,0},{0,-2,0},{0,-3,0},{0,-4,0},{1,3,0},{1,2,0},{1,1,0},{1,0,0},{1,-1,0},{1,-2,0},{1,-3,0},{1,-4,0},{2,2,0},{2,1,0},{2,0,0},{2,-1,0},{2,-2,0},{2,-3,0},{2,-4,0},{3,1,0},{3,0,0},{3,-1,0},{3,-2,0},{3,-3,0},{3,-4,0},{4,0,0},{4,-1,0},{4,-2,0},{4,-3,0},{4,-4,0},{1,-5,1},{2,-5,1},{3,-5,1},{4,-5,1},{2,-6,1},{3,-6,1},{4,-6,1},{3,-7,1},{4,-7,1},{4,-8,1},{-4,-1,2},{-3,-2,2},{-2,-3,2},{-1,-4,2},{-4,-2,2},{-3,-3,2},{-2,-4,2},{-4,-3,2},{-3,-4,2},{-4,-4,2},{-5,4,3},{-5,3,3},{-5,2,3},{-5,1,3},{-6,4,3},{-6,3,3},{-6,2,3},{-7,4,3},{-7,3,3},{-8,4,3},{-1,5,4},{-2,5,4},{-3,5,4},{-4,5,4},{-2,6,4},{-3,6,4},{-4,6,4},{-3,7,4},{-4,7,4},{-4,8,4},{4,1,5},{3,2,5},{2,3,5},{1,4,5},{4,2,5},{3,3,5},{2,4,5},{4,3,5},{3,4,5},{4,4,5},{5,-4,6},{5,-3,6},{5,-2,6},{5,-1,6},{6,-4,6},{6,-3,6},{6,-2,6},{7,-4,6},{7,-3,6},{8,-4,6}} );
//        BoardView boardView = new BoardView(new int[][]{{0, 1, 1}, {-1,1,2}, {-1,0,3}, {0,-1,4}, {1,-1,5}, {1,0,6}});
        JFrame frame = new JFrame();
        frame.add(boardView);
        frame.setSize(BoardView.WINDOW_WIDTH, BoardView.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        //

        frame.getContentPane().add(messageLabel, BorderLayout.SOUTH);
    }*/
}