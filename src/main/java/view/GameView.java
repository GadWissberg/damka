package view;

import controller.*;
import interfaces.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class GameView extends JPanel {
    private static final String IMAGE_BACKGROUND = GameWindow.RSC_FOLDER + "background.jpg";
    private static final String MSG_LOAD_UP_FAILURE = "The game has failed to load up!";
    private static final String LABEL_CURRENT_TURN = "Current turn: %s";

    private BufferedImage backgroundImage;
    private Controller controller;
    private DamkaLabel currentTurnLabel;

    public GameView(GridBagLayout gridBagLayout, Controller controller) {
        super(gridBagLayout);
        this.controller = controller;
        try {
            backgroundImage = ImageIO.read(new File(IMAGE_BACKGROUND));
            createWindowContent();
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

    private void createWindowContent() {
        try {
            addLabel(new ScoreLabel(controller.getPlayer1()), 0, 0);
            addLabel(new ScoreLabel(controller.getPlayer2()), 0, 2);
            currentTurnLabel = new DamkaLabel("");
            updateCurrentTurnLabel();
            addLabel(currentTurnLabel, 1, 0);
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
        if (controller != null) {
            updateCurrentTurnLabel();
        }
    }

    private void updateCurrentTurnLabel() {
        Player currentTurn = controller.getCurrentTurn();
        currentTurnLabel.setText(String.format(LABEL_CURRENT_TURN, currentTurn.getName()));
        currentTurnLabel.setForeground(currentTurn.getColor());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }
    }
}
