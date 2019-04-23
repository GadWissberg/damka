package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoardView extends JPanel {
    private static final int CELLS_IN_ROW = 8;
    private static final String RSC_FOLDER = "src\\main\\resources\\";
    private static final String IMAGE_WHITE_CELL = RSC_FOLDER + "white_cell.png";
    private static final String IMAGE_BLACK_CELL = RSC_FOLDER + "black_cell.png";
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    private BufferedImage whiteCell;
    private BufferedImage blackCell;
    private Stroke borderStroke = new BasicStroke(8);

    BoardView(MouseListener inputConsumer) throws IOException {
        whiteCell = ImageIO.read(new File(IMAGE_WHITE_CELL));
        blackCell = ImageIO.read(new File(IMAGE_BLACK_CELL));
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        addMouseListener(inputConsumer);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D gt = (Graphics2D) g;
        drawCells(g);
        g.setColor(Color.BLACK);
        gt.setStroke(borderStroke);
        g.drawRect(0, 0, 512, 512);
    }

    private void drawCells(Graphics g) {
        for (int i = 0; i < CELLS_IN_ROW * CELLS_IN_ROW; i++) {
            int currentRow = i / CELLS_IN_ROW;
            boolean isEven = i % 2 == 0;
            BufferedImage cell = currentRow % 2 == 0 ? (isEven ? whiteCell : blackCell) : isEven ? blackCell : whiteCell;
            int x = (i % CELLS_IN_ROW) * cell.getWidth();
            int y = currentRow * cell.getHeight();
            g.drawImage(cell, x, y, null);
        }
    }

}
