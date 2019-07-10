import controller.*;
import org.junit.*;

import static org.junit.Assert.*;


public class DamkaTest {

    @Test()
    public void testBoardClass() {
        Board board = new Board();
//        board.fillBoard(p1, p2);
        board.printBoard();
        assertEquals(board.getNumOfBluePawns(), 12);
        assertEquals(board.getNumOfRedPawns(), 12);
        assertTrue(board.spotOnBoardIsFree(0,0));
        assertFalse(board.spotOnBoardIsFree(0,1));
    }

}