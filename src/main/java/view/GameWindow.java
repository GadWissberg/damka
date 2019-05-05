package view;

import logic.Session;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameWindow extends JFrame {

    public static final String RSC_FOLDER = "src\\main\\resources\\";
    private static final String MSG_LOAD_UP_FAILURE = "The game has failed to load up!";
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final String LABEL_CURRENT_TURN = "Current turn: %s";

    public void initialize(Session session) {
        setVisible(true);
        createWindowContent(session);
        pack();
    }

    private void createWindowContent(Session session) {
        GameView gamePanel = new GameView(new GridBagLayout());
        gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        try {
            addLabel(gamePanel, new ScoreLabel(session.getPlayer1()), 0, 0);
            addLabel(gamePanel, new ScoreLabel(session.getPlayer2()), 0, 2);
            String currentTurnText = String.format(LABEL_CURRENT_TURN, session.getCurrentTurn().getName());
            addLabel(gamePanel, new DamkaLabel(currentTurnText), 1, 0);
            createBoard(session, gamePanel);
            add(gamePanel);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, MSG_LOAD_UP_FAILURE);
            System.exit(0);
        }
    }

    private void createBoard(Session session, JPanel gamePanel) throws IOException {
        BoardView boardView;
        boardView = new BoardView(session);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        gamePanel.add(boardView, constraints);
    }

    private void addLabel(JPanel gamePanel, JLabel label, int gridX, int gridY) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        gamePanel.add(label, constraints);
    }

}
