package logic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Session implements MouseListener {
    private Player p1;
    private Player p2;
    PawnInterface selectedPawn;
    private Board board = new Board();
    private Player turn;

    public void initialize(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        turn = Math.random() > 0.5f ? p1 : p2;
        board.fillBoard();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("Mouse clicked: X:" + x + ", Y:" + y);
//        Object cell = board.getCellByCoords(x, y); // TODO: This should return a cell object by X and Y.
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
}
