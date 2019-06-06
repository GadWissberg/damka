package logic;

import interfaces.Controller;
import interfaces.DamkaDisplay;
import logic.pawn.Pawn;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Session implements Controller {
    private Player p1;
    private Player p2;
    private Board board = new Board();
    private Player turn;
    private Rectangle cellSize = new Rectangle();
    private ArrayList<DamkaDisplay> displays = new ArrayList<>();

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
        Pawn selectedPawn = board.getSelectedPawn();
        if (selectedPawn == null) {
            tryToSelectPawn(row, column);
        } else {
            //getSelectedPawnLegitActions.
            board.movePawn(selectedPawn, row, column);
            board.setSelectedPawn(null);
            turn = turn == p1 ? p2 : p1;
            displays.forEach(damkaDisplay -> {
                damkaDisplay.refreshDisplay();
                damkaDisplay.setSelectionImageVisibility(false);
            });
        }
    }

    private void tryToSelectPawn(int row, int column) {
        Pawn pawnAtPosition = board.getPawnAtPosition(row, column);
        if (pawnAtPosition != null && pawnAtPosition.getPlayer() == turn) {
            board.setSelectedPawn(pawnAtPosition);
            displays.forEach(subscriber -> subscriber.setSelectionImage(column * cellSize.getWidth(),
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
        Pawn p;
        p = this.board.getSelectedPawn();
        if(p == null) // no pawn selected
            return null;

        ArrayList<Move> arr = new ArrayList();
        BoardPosition pawnPos = p.getPosition();
        int r, c;

        int pCol = pawnPos.getCol();
        int pRow = pawnPos.getRow();
        if(p.getPlayer() == p1 && pRow+1 <= board.CELLS_IN_ROW) { // logic for player 1
            if (pCol - 1 >= 0) { // left move in bounds
                p = board.getPawnAtPosition(pRow + 1, pCol - 1);
                if (p == null) // no player, move freely
                    arr.add(new Move(new BoardPosition(pRow + 1, pCol - 1), Move.MoveType.REGULAR));
                else if (p.getPlayer() == p2 && pRow + 2 <= board.CELLS_IN_ROW && pCol - 2 >= 0) // can eat
                    arr.add(new Move(new BoardPosition(pRow + 2, pCol - 2), Move.MoveType.EAT));
            }

            if (pCol + 1 <= board.CELLS_IN_ROW) { // right move in bounds
                p = board.getPawnAtPosition(pRow+1, pCol+1);
                if(p == null) // move freely
                    arr.add(new Move(new BoardPosition(pRow + 1, pCol + 1), Move.MoveType.REGULAR));
                else if(p.getPlayer() == p2 && pRow+2 <= board.CELLS_IN_ROW && pCol+2 <= board.CELLS_IN_ROW) // can eat
                    arr.add(new Move(new BoardPosition(pRow + 2, pCol + 2), Move.MoveType.EAT));
            }
        } else { // logic for player 2
        }

        return arr;
    }

    @Override
    public void setCellSize(int width, int height) {
        cellSize.setSize(width, height);
    }

    @Override
    public void subscribeForOutput(DamkaDisplay subscriber) {
        if (!displays.contains(subscriber)) {
            displays.add(subscriber);
        }
    }
}
