import logic.Board;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class DamkaTest {

    @Test()
    public void testBoardClass() {
        Board board = new Board();
        board.fillBoard();
        board.printBoard();
        assertEquals(board.getNumOfBluePawns(), 12);
        assertEquals(board.getNumOfRedPawns(), 12);
        assertTrue(board.spotOnBoardIsFree(0,0));
        assertFalse(board.spotOnBoardIsFree(0,1));
    }

}