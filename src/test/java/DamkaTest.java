import controller.*;
import org.junit.*;

import java.awt.*;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;


public class DamkaTest {

    private Board board;
    private Player p1;
    private Player p2;
    private JsonHandler jsonHandler;

    @Before
    public void initObjects() {
        board = new Board();
        p1 = new Player("test 1", Color.RED, Player.Direction.DOWN);
        p2 = new Player("test 2", Color.BLUE, Player.Direction.UP);
        jsonHandler = new JsonHandler();
    }

    @Test()
    public void testBoardClass() {
        board.fillBoard(p1, p2);
        assertTrue("The spot was not free", board.spotOnBoardIsFree(0,0));
        assertFalse("The spot was not empty", board.spotOnBoardIsFree(0,1));
        assertEquals("number of player 1 pawns is wrong", board.getNumOfBluePawns(), 12);
        assertEquals("number of player 2 pawns is wrong", board.getNumOfRedPawns(), 12);
    }

    @Test()
    public void testTotalScores() throws FileNotFoundException {
        if (jsonHandler.getScoreOne() < 0 )
            fail("Negative Score for player one was loaded from file");
        if (jsonHandler.getScoreTwo() < 0 )
            fail("Negative Score for player two was loaded from file");
    }

    @Test()
    public void testPlayerName() {
        assertNotEquals("Player 1 name is empty", "", p1.getName());
        assertNotEquals("Player 2 name is empty", "", p2.getName());
    }

}