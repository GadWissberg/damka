package view;

import interfaces.ViewListener;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.File;

public class GameWindow extends JFrame {

    public static final String RSC_FOLDER = "src" + File.separator + "main" + File.separator + "resources" + File.separator;
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    public void initialize(ViewListener mouseListener, PropertyChangeListener listener) {
        setVisible(true);
        GameView gamePanel = new GameView(new GridBagLayout(), mouseListener);
        gamePanel.addPropertyChangeListener(listener);
        gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        add(gamePanel);
        pack();
    }

}
