import logic.Board;
import logic.Player;
import logic.Session;
import view.GameWindow;

import javax.swing.*;
import java.awt.*;

public class Damka {
    public static void main(String... args) {
        /* ----------- SESSION TESTING ------------- */
        Session session = new Session();
        GameWindow display = new GameWindow();
        Player p1 = new Player("Trump", Color.RED);
        Player p2 = new Player("Bibi", Color.BLUE);
        session.initialize(p1, p2);
        display.initialize(session);
        display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /* ----------- BOARD TESTING ------------ */
        Board board = new Board();
        board.fillBoard();
        board.printBoard();
        System.out.println(board.getNumOfBluePawns());
        System.out.println(board.getNumOfRedPawns());
        if (board.spotOnBoardIsFree(0,0))
            System.out.println("This spot is free");

    }
}
