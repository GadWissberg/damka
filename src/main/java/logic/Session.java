package logic;

import interfaces.InputConsumer;
import interfaces.OutputSubscriber;
import logic.pawn.Pawn;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Session implements InputConsumer {
    private Player p1;
    private Player p2;
    private Board board = new Board();
    private Player turn;
    private Rectangle cellSize = new Rectangle();
    private ArrayList<OutputSubscriber> subscribersForOutput = new ArrayList<>();

    public void initialize(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        turn = Math.random() > 0.5f ? p1 : p2;
        board.fillBoard(p1, p2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = (int) (e.getY() / cellSize.getWidth());
        int column = (int) (e.getX() / cellSize.getHeight());
        Pawn pawnAtPosition = board.getPawnAtPosition(row, column);
        if (pawnAtPosition != null && pawnAtPosition.getPlayer() == turn) {
            board.setSelectedPawn(pawnAtPosition);
            subscribersForOutput.forEach(subscriber -> subscriber.setSelectionImage(column * cellSize.getWidth(),
                    row * cellSize.getHeight()));
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

    private ArrayList canMoveto() {
        Pawn selectedPawn;
        selectedPawn = this.board.getSelectedPawn();
        if(selectedPawn == null) // no pawn selected
            return null;

        BoardPosition pawnPos = selectedPawn.getPosition();
        int r;
        for(r = pawnPos.getRow(); r < pawnPos.getRow()+2; r++) {

        }




        ArrayList<BoardPosition> arr = new ArrayList();



        arr.add(new BoardPosition(1,1));

        return arr;
    }

    @Override
    public void setCellSize(int width, int height) {
        cellSize.setSize(width, height);
    }

    @Override
    public void subscribeForOutput(OutputSubscriber subscriber) {
        if (!subscribersForOutput.contains(subscriber)) {
            subscribersForOutput.add(subscriber);
        }
    }
}
