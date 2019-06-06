package view;

import interfaces.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameView extends JPanel {
    private static final String IMAGE_BACKGROUND = GameWindow.RSC_FOLDER + "background.jpg";
    private static final String MSG_LOAD_UP_FAILURE = "The game has failed to load up!";
    private static final String LABEL_CURRENT_TURN = "Current turn: %s";

    private BufferedImage backgroundImage;

    public GameView(GridBagLayout gridBagLayout, Controller controller) {
        super(gridBagLayout);
        try {
            backgroundImage = ImageIO.read(new File(IMAGE_BACKGROUND));
            createWindowContent(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createBoard(Controller controller) throws IOException {
        BoardView boardView;
        boardView = new BoardView(controller, controller.getBoard());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(boardView, constraints);
    }

    private void addLabel(JLabel label, int gridX, int gridY) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        add(label, constraints);
    }

    private void createWindowContent(Controller controller) {
        try {
            addLabel(new ScoreLabel(controller.getPlayer1()), 0, 0);
            addLabel(new ScoreLabel(controller.getPlayer2()), 0, 2);
            String currentTurnText = String.format(LABEL_CURRENT_TURN, controller.getCurrentTurn().getName());
            addLabel(new DamkaLabel(currentTurnText), 1, 0);
            createBoard(controller);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, MSG_LOAD_UP_FAILURE);
            System.exit(0);
        }
    }

    @Override
    public void repaint() {
        super.repaint();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }
    }
}
