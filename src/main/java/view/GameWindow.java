package view;

import interfaces.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameWindow extends JFrame {

    public static final String RSC_FOLDER = "src"+File.separator+"main"+File.separator+"resources"+File.separator;
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    public void initialize(Controller controller) {
        setVisible(true);
        GameView gamePanel = new GameView(new GridBagLayout(), controller);
        gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        add(gamePanel);
        pack();
    }

}
