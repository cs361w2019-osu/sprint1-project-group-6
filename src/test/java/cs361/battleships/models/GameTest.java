package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.*;

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

    @Test
    public void testSpace() {
        Game game = new Game();
        assertTrue(game.space(1, 'A'));
    }

    @Test
    public void testMove() {
        Game game = new Game();
        assertTrue(game.move(1));
    }

    @Test
    public void testVertical() {
        Game game = new Game();
        boolean randVert = game.randVertical();
        assertNotNull(randVert);
    }

    @Test
    public void testAttack () {
        Game game = new Game();
        boolean atk1 = game.attack(0, 'A');
        assertTrue(atk1);
    }

    @Test
    public void testShipPlacement() {
        Game game = new Game();
        Ship ship = new Ship("MINESWEEPER");
        boolean ship1 = game.placeShip(ship, 0, 'A', false);
        boolean ship2 = game.placeShip(ship, 5, 'D', false);
        assertTrue(ship1);
        assertFalse(ship2);
    }
}

