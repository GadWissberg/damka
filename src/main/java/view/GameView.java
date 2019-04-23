package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;

public class GameView extends JFrame {

    private static final String MSG_LOAD_UP_FAILURE = "The game has failed to load up!";
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    public void initialize(MouseListener consumer) {
        setVisible(true);
        createWindowContent(consumer);
        pack();
    }

    private void createWindowContent(MouseListener consumer) {
        JPanel gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        BoardView boardView;
        try {
            boardView = new BoardView(consumer);
            gamePanel.add(boardView);
            add(gamePanel);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, MSG_LOAD_UP_FAILURE);
            System.exit(0);
        }
    }

}
