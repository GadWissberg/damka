package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameView extends JPanel {
    private static final String IMAGE_BACKGROUND = GameWindow.RSC_FOLDER + "background.jpg";
    private BufferedImage backgroundImage;

    public GameView(GridBagLayout gridBagLayout) {
        super(gridBagLayout);
        try {
            backgroundImage = ImageIO.read(new File(IMAGE_BACKGROUND));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }
    }
}
