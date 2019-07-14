package view;

import controller.Player;
import interfaces.ViewListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;

public class GameView extends JPanel {
    private static final String IMAGE_BACKGROUND = GameWindow.RSC_FOLDER + "background.jpg";
    private static final String MSG_LOAD_UP_FAILURE = "The game has failed to load up!";
    private static final String LABEL_CURRENT_TURN = "Current turn: %s";

    private BufferedImage backgroundImage;
    private ViewListener viewListener;
    private PropertyChangeSupport propertyChangeHandler;
    private DamkaLabel currentTurnLabel;

    public GameView(GridBagLayout gridBagLayout, ViewListener mouseListener) {
        super(gridBagLayout);
        this.propertyChangeHandler = new PropertyChangeSupport(this);
        this.viewListener = mouseListener;
        try {
            backgroundImage = ImageIO.read(new File(IMAGE_BACKGROUND));
            createWindowContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeHandler.addPropertyChangeListener(listener);
    }

    private void createBoard(ViewListener viewListener) throws IOException {
        BoardView boardView;
        boardView = new BoardView(viewListener, viewListener.getBoard());
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

    private void addPanel(JPanel panel, int gridX, int gridY) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        add(panel, constraints);
    }

    private void createWindowContent() {
        try {
            addLabel(new ScoreLabel(viewListener.getPlayer1()), 0, 0);
            addLabel(new ScoreLabel(viewListener.getPlayer2()), 0, 2);
            currentTurnLabel = new DamkaLabel("");
            updateCurrentTurnLabel();
            addLabel(currentTurnLabel, 1, 0);
            JPanel buttons = createViewButtons();
            addPanel(buttons, 3, 0);
            createBoard(viewListener);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, MSG_LOAD_UP_FAILURE);
            System.exit(0);
        }
    }

    private JPanel createViewButtons() {
        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(0, 0, 0, 0));
        buttons.add(createRestartButton());
        buttons.add(createShowStatButton());
        buttons.add(createSaveGameButton());
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        return buttons;
    }

    private JButton createSaveGameButton() {
        JButton showGameStat = new JButton("Save Game");
        showGameStat.addActionListener(e -> propertyChangeHandler.firePropertyChange("Save Game", false, true));
        return showGameStat;
    }

    private JButton createShowStatButton() {
        JButton showGameStat = new JButton("Show games statistics");
        showGameStat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleGamesStat();
            }
        });
        return showGameStat;
    }

    private void toggleGamesStat() {
        //TODO change this toggle
        String name = JOptionPane.showInputDialog(this, "need to change this", null);
    }

    private JButton createRestartButton() {
        JButton gameRestart = new JButton("Restart game");
        gameRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                propertyChangeHandler.firePropertyChange("Restart button", false, true);
            }
        });
        return gameRestart;
    }

    @Override
    public void repaint() {
        super.repaint();
        if (viewListener != null) {
            updateCurrentTurnLabel();
        }
    }

    private void updateCurrentTurnLabel() {
        Player currentTurn = viewListener.getCurrentTurn();
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
