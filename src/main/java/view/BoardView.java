package view;

import logic.Board;
import logic.pawn.PawnInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoardView extends JPanel {
    private static final String IMAGE_WHITE_CELL_PATH = GameWindow.RSC_FOLDER + "white_cell.png";
    private static final String IMAGE_BLACK_CELL_PATH = GameWindow.RSC_FOLDER + "black_cell.png";
    private static final String IMAGE_BLUE_PAWN_PATH = GameWindow.RSC_FOLDER + "blue.png";
    private static final String IMAGE_RED_PAWN_PATH = GameWindow.RSC_FOLDER + "red.png";
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    private final Board board;

    private BufferedImage whiteCellImage;
    private BufferedImage blackCellImage;
    private BufferedImage bluePawnImage;
    private BufferedImage redPawnImage;
    private Stroke borderStroke = new BasicStroke(8);

    BoardView(MouseListener inputConsumer, Board board) throws IOException {
        this.board = board;
        initializeImages();
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        addMouseListener(inputConsumer);
    }

    private void initializeImages() throws IOException {
        whiteCellImage = ImageIO.read(new File(IMAGE_WHITE_CELL_PATH));
        blackCellImage = ImageIO.read(new File(IMAGE_BLACK_CELL_PATH));
        bluePawnImage = ImageIO.read(new File(IMAGE_BLUE_PAWN_PATH));
        redPawnImage = ImageIO.read(new File(IMAGE_RED_PAWN_PATH));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D gt = (Graphics2D) g;
        drawCellsAndPawns(g);
        g.setColor(Color.BLACK);
        gt.setStroke(borderStroke);
        g.drawRect(0, 0, 512, 512);
    }

    private void drawCellsAndPawns(Graphics g) {
        for (int i = 0; i < Board.CELLS_IN_ROW * Board.CELLS_IN_ROW; i++) {
            drawCell(i, g);
            drawPawn(i, g);
        }
    }

    private void drawPawn(int i, Graphics g) {
        int currentRow = i / Board.CELLS_IN_ROW;
        int currentCol = i % Board.CELLS_IN_ROW;
        int x = (i % Board.CELLS_IN_ROW) * whiteCellImage.getWidth();
        int y = currentRow * whiteCellImage.getHeight();
        PawnInterface pawnAtCurrentCell = board.getPawnAtPosition(currentRow, currentCol);
        if (pawnAtCurrentCell != null) {
            g.drawImage(pawnAtCurrentCell.getPlayer().getColor() == Color.RED ? redPawnImage : bluePawnImage, x, y, null);
        }
    }

    private void drawCell(int i, Graphics g) {
        int currentRow = i / Board.CELLS_IN_ROW;
        boolean isIndexEven = i % 2 == 0;
        boolean isCurrentRowEven = currentRow % 2 == 0;
        BufferedImage cell = isCurrentRowEven ? (isIndexEven ? blackCellImage : whiteCellImage)
                : isIndexEven ? whiteCellImage : blackCellImage;
        int x = (i % Board.CELLS_IN_ROW) * cell.getWidth();
        int y = currentRow * cell.getHeight();
        g.drawImage(cell, x, y, null);
    }

}
