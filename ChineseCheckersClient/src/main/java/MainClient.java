
import view.BoardView;
import view.CellState;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.Socket;

public class MainClient extends JFrame {

    private Socket socket;
    private final ClientCommunicationService communicationService;

    private final JLabel messageLabel;
    private final Container boardViewContainer;
    private BoardView boardView;

    public MainClient() {
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), 58901);
        } catch (Exception e) {
            System.out.println("Connection refused, try again");
            System.exit(1);
        }
        this.communicationService = new ClientCommunicationService(this, this.socket);

        boardViewContainer = new Container();
        this.messageLabel = new JLabel("Waiting for game to start");

        JButton passButton = new JButton("Pass");
        passButton.addActionListener(e -> this.communicationService.send("PASS"));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            this.communicationService.send("QUIT");
            this.dispose();
            System.exit(1);
        });

        this.setLayout(new BorderLayout());
        this.add(boardViewContainer, BorderLayout.CENTER);
        this.add(messageLabel, BorderLayout.SOUTH);
        this.add(passButton, BorderLayout.EAST);
        this.add(closeButton, BorderLayout.NORTH);

        this.setTitle("Chinese Checkers - player");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(820, 820);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void setMessageLabel(String message) {
        this.messageLabel.setText(message);
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public void setStartingBoard(int[][] gameInfo) {
        BoardView boardView = new BoardView(gameInfo);
        boardView.addMouseListener(new BoardController(this.communicationService, boardView));

        this.boardView = boardView;
        this.boardViewContainer.add(boardView);
        this.boardView.repaint();
    }

    public void setPlayer(int playerId) {
        this.setTitle(this.getTitle() + playerId);
        this.messageLabel.setForeground(CellState.getColorById(playerId));
    }

    public void start() throws Exception {
        try {
            communicationService.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
            this.dispose();
        }
    }

    public static void main(String[] args) throws Exception {
        MainClient client = new MainClient();
        client.start();
    }
}