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
        int max=10;
        int min=1;
        for(i=0 ;i< 100 ;i++) //Run the function 100times
        {
            game.randCol();
            assertTrue(min <= game.randCol() <= max );
        }
    }

    @Test
    public void testRandomRow() {
        Game game = new Game();
        for (i = 0; i < 100; i++) {
            game.randRow();
            assertTrue(1 <= game.randRow()<=10)
        }
    }

    }
}
