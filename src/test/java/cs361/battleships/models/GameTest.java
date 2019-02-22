package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testRandomColumn() {
        Game game = new Game();
        char max='J';
        char min='A';
        for(int i=0 ;i< 100 ;i++) //Run the function 100times
        {
            int randCol = game.randCol();
            assertTrue(min <= randCol && randCol <= max );
        }
    }

    @Test
    public void testRandomRow() {
        Game game = new Game();
        for (int i = 0; i < 100; i++) {
            int randRow = game.randRow();
            assertTrue(1 <= randRow && randRow <= 10);
        }
    }
//    
//    @Test
//    public void testVertical() {
//        Game game = new Game();
//        boolean randVert = game.randVertical();
//        assertTrue(randVert);
//    }
}

