package logic;

import interfaces.InputConsumer;
import interfaces.OutputSubscriber;
import logic.pawn.Pawn;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Session implements InputConsumer {
    private Player p1;
    private Player p2;
    Pawn selectedPawn;
    private Board board = new Board();
    private Player turn;
    private int cellWidth;
    private int cellHeight;
    private ArrayList<OutputSubscriber> subscribersForOutput = new ArrayList<>();

    public void initialize(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        turn = Math.random() > 0.5f ? p1 : p2;
        board.fillBoard(p1, p2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = e.getY() / cellWidth;
        int column = e.getX() / cellHeight;
        Pawn pawnAtPosition = board.getPawnAtPosition(row, column);
        if (pawnAtPosition != null && pawnAtPosition.getPlayer() == turn) {
            selectedPawn = pawnAtPosition;
            subscribersForOutput.forEach(subscriber -> subscriber.setSelectionImage(column * cellWidth,
                    row * cellHeight));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
//Nothing yet...
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//Nothing yet...
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//Nothing yet...
    }

    @Override
    public void mouseExited(MouseEvent e) {
//Nothing yet...
    }

    public Player getPlayer1() {
        return p1;
    }

    public Player getPlayer2() {
        return p2;
    }

    public Player getCurrentTurn() {
        return turn;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void setCellSize(int width, int height) {
        cellWidth = width;
        cellHeight = height;
    }

    @Override
    public void subscribeForOutput(OutputSubscriber subscriber) {
        if (!subscribersForOutput.contains(subscriber)) {
            subscribersForOutput.add(subscriber);
        }
    }
}
