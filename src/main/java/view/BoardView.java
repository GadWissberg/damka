package view;

import controller.Board;
import interfaces.DamkaDisplay;
import interfaces.ViewListener;
import model.BoardPixelLocation;
import model.Pawn;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BoardView extends JPanel implements DamkaDisplay {
    private static final String IMAGE_WHITE_CELL_PATH = GameWindow.RSC_FOLDER + "white_cell.png";
    private static final String IMAGE_BLACK_CELL_PATH = GameWindow.RSC_FOLDER + "black_cell.png";
    private static final String IMAGE_BLUE_PAWN_PATH = GameWindow.RSC_FOLDER + "blue.png";
    private static final String IMAGE_RED_PAWN_PATH = GameWindow.RSC_FOLDER + "red.png";
    private static final String IMAGE_QUEEN_PATH = GameWindow.RSC_FOLDER + "queen.png";
    private static final String IMAGE_SELECTION_PATH = GameWindow.RSC_FOLDER + "selection.png";
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    private final Board board; //TODO: REMOVE THIS FROM HERE!

    private BufferedImage whiteCellImage;
    private BufferedImage blackCellImage;
    private BufferedImage queenImage;
    private BufferedImage bluePawnImage;
    private BufferedImage redPawnImage;
    private BufferedImage selectionImage;
    private Stroke borderStroke = new BasicStroke(8);
    private SelectionComponent selectionComponent = new SelectionComponent();
    private ArrayList<SelectionComponent> availableMovesIndicators = new ArrayList<>();

    BoardView(ViewListener viewListener, Board board) throws IOException {
        this.board = board;
        initializeImages();
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        addMouseListener(viewListener);
        viewListener.setCellSize(whiteCellImage.getWidth(), whiteCellImage.getHeight());
        viewListener.subscribeForOutput(this);
    }

    private void initializeImages() throws IOException {
        whiteCellImage = ImageIO.read(new File(IMAGE_WHITE_CELL_PATH));
        blackCellImage = ImageIO.read(new File(IMAGE_BLACK_CELL_PATH));
        bluePawnImage = ImageIO.read(new File(IMAGE_BLUE_PAWN_PATH));
        redPawnImage = ImageIO.read(new File(IMAGE_RED_PAWN_PATH));
        queenImage = ImageIO.read(new File(IMAGE_QUEEN_PATH));
        selectionImage = ImageIO.read(new File(IMAGE_SELECTION_PATH));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D gt = (Graphics2D) g;
        drawCellsAndPawns(g);
        g.setColor(Color.BLACK);
        gt.setStroke(borderStroke);
        g.drawRect(0, 0, 512, 512);
        drawSelectionComponents(g);
    }

    private void drawSelectionComponents(Graphics g) {
        if (selectionComponent.visible)
            g.drawImage(selectionImage, (int) selectionComponent.getX(), (int) selectionComponent.getY(), null);
        availableMovesIndicators.forEach(cell -> g.drawImage(selectionImage, (int) cell.getX(), (int) cell.getY(), null));
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
        Pawn pawnAtCurrentCell = board.getPawnAtPosition(currentRow, currentCol);
        if (pawnAtCurrentCell != null) {
            g.drawImage(pawnAtCurrentCell.getPlayer().getColor() == Color.RED ? redPawnImage : bluePawnImage, x, y, null);
            if (pawnAtCurrentCell.isQueen()) {
                g.drawImage(queenImage, x, y, null);
            }
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

    @Override
    public void setSelectionImage(double x, double y) {
        selectionComponent.setPosition(x, y);
        selectionComponent.setVisible(true);
        repaint();
    }

    @Override
    public void refreshDisplay() {
        getParent().repaint();
        repaint();
    }

    @Override
    public void setSelectionImageVisibility(boolean b) {
        selectionComponent.setVisible(b);
    }

    @Override
    public void displayMessage(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

    @Override
    public void setAvailableMovesLocations(ArrayList<BoardPixelLocation> canMoveto) {
        availableMovesIndicators.clear();
        if (canMoveto != null) {
            canMoveto.forEach(location -> {
                SelectionComponent selectionComponent = new SelectionComponent();
                selectionComponent.setPosition(location);
                availableMovesIndicators.add(selectionComponent);
            });
            refreshDisplay();
        }
    }

    private class SelectionComponent extends BoardPixelLocation {
        private boolean visible;

        public SelectionComponent() {
            super(0, 0);
            setVisible(false);
        }

        public void setPosition(BoardPixelLocation position) {
            super.setPosition(position.getX(), position.getY());
        }

        public void setVisible(boolean b) {
            visible = b;
        }
    }
}
