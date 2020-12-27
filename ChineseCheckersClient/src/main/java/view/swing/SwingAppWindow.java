package view.swing;

import app.MainClient;
import view.AppWindow;
import app.BoardController;
import view.BoardView;
import view.CellState;

import javax.swing.*;
import java.awt.*;

public class SwingAppWindow extends JFrame implements AppWindow {

    private final MainClient app;

    private final JLabel messageLabel;
    private final JButton closeButton;
    private final JButton passButton;
    private final Container boardViewContainer;
    private SwingBoardView boardView;

    public SwingAppWindow(MainClient app) {
        this.app = app;

        this.boardViewContainer = new Container();
        JLabel logo = new JLabel();
        logo.setSize(620, 620);
        logo.setIcon(new ImageIcon(new ImageIcon("ChineseCheckersClient/src/main/resources/icon2.png").getImage().getScaledInstance(620, 620, Image.SCALE_SMOOTH)));

        this.boardViewContainer.add(logo);
        this.messageLabel = new JLabel("Waiting for game to start");
        this.messageLabel.setForeground(CellState.getStateById(0).getColor());

        this.passButton = new JButton("Pass");
        this.passButton.setFocusable(false);
        this.passButton.addActionListener(e -> this.app.getCommunicationService().send("PASS"));
        this.passButton.setEnabled(false);

        this.closeButton = new JButton("Close");
        this.closeButton.setFocusable(false);
        this.closeButton.addActionListener(e -> {
            this.app.getCommunicationService().send("QUIT");
            this.dispose();
            System.exit(1);
        });

        this.setLayout(new BorderLayout());
        this.add(boardViewContainer, BorderLayout.CENTER);
        this.add(messageLabel, BorderLayout.SOUTH);
        this.add(passButton, BorderLayout.EAST);
        this.add(closeButton, BorderLayout.NORTH);

        this.setTitle("Chinese Checkers - player");
        this.setIconImage(new ImageIcon("ChineseCheckersClient/src/main/resources/icon4.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(758, 790);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void setMessage(String message) {
        this.messageLabel.setText(message);
    }

    @Override
    public BoardView getBoardView() {
        return boardView;
    }

    @Override
    public void setStartingBoard(int[][] gameInfo) {
        this.boardViewContainer.removeAll();
        this.passButton.setEnabled(true);

        SwingBoardView boardView = new SwingBoardView(gameInfo);
        boardView.addMouseListener(new BoardController(this.app.getCommunicationService(), boardView));

        this.boardView = boardView;
        this.boardViewContainer.add(boardView);
        this.boardView.repaint();
    }

    @Override
    public void setPlayer(int playerId) {
        this.setTitle(this.getTitle() + playerId);
        this.passButton.setForeground(CellState.getStateById(playerId).getColor());
        this.closeButton.setForeground(CellState.getStateById(playerId).getColor());
        this.getContentPane().setBackground(CellState.getStateById(playerId).getColor());
    }

    @Override
    public void close() {
        this.dispose();
    }
}
