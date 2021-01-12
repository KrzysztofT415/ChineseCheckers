package view.swing;

import appClient.MainClient;
import view.AppWindow;
import appClient.BoardController;
import view.BoardView;
import view.CellState;

import javax.swing.*;
import java.awt.*;

public class SwingAppWindowWatching extends JFrame implements AppWindow {

    private final MainClient app;

    private final JLabel messageLabel;
    private final JButton closeButton;
    private final JButton backButton;
    private final JButton nextButton;
    private final Container boardViewContainer;
    private SwingBoardView boardView;

    public SwingAppWindowWatching(MainClient app) {
        this.app = app;

        this.boardViewContainer = new Container();

        this.messageLabel = new JLabel("Watching mode");
        this.messageLabel.setForeground(CellState.getStateById(0).getColor());

        this.backButton = new JButton("Back");
        this.backButton.setFocusable(false);
        this.backButton.addActionListener(e -> this.app.getCommunicationService().send("BACK"));
        this.backButton.setEnabled(false);

        this.nextButton = new JButton("Next");
        this.nextButton.setFocusable(false);
        this.nextButton.addActionListener(e -> this.app.getCommunicationService().send("NEXT"));
        this.nextButton.setEnabled(false);

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
        this.add(nextButton, BorderLayout.EAST);
        this.add(backButton, BorderLayout.WEST);
        this.add(closeButton, BorderLayout.NORTH);

        this.setTitle("Chinese Checkers - spectator");
        this.setIconImage(new ImageIcon("ChineseCheckersClient/src/main/resources/icon.png").getImage());
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
    public void showMessageWindow(String message) { }

    @Override
    public BoardView getBoardView() {
        return boardView;
    }

    @Override
    public void setStartingBoard(int[][] gameInfo) {
        this.nextButton.setEnabled(true);
        this.backButton.setEnabled(true);

        SwingBoardView boardView = new SwingBoardView(gameInfo);
        boardView.addMouseListener(new BoardController(this.app.getCommunicationService(), boardView));

        this.boardView = boardView;
        this.boardViewContainer.add(boardView);
        this.boardView.repaint();
    }

    @Override
    public void setPlayer(int playerId) {
        this.nextButton.setForeground(CellState.getStateById(3).getColor());
        this.backButton.setForeground(CellState.getStateById(3).getColor());
        this.closeButton.setForeground(CellState.getStateById(3).getColor());
        this.getContentPane().setBackground(CellState.getStateById(3).getColor());
    }

    @Override
    public void close() {
        this.dispose();
    }
}