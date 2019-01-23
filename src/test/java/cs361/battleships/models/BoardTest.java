package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testPlacementBounds() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
        board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 10, 'K', true));
        board = new Board();
        assertFalse(board.placeShip(new Ship("DESTROYER"), 1, 'I', false));
        board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 10, 'A', false));
        board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'J', true));
    }

    @Test
    public void testTwoShipsSameLoc() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 5, 'C', true));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 4, 'C', true));
    }

    @Test
    public void testSameShipType() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 5, 'C', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', false));
    }

    @Test
    public void testAttackBounds() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'C', true);
        assertTrue(board.attack(15, 'C').getResult() == AtackStatus.INVALID);
    }

    @Test
    public void testAttackSameLoc() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'C', true);
        board.attack(1, 'C');
        assertTrue(board.attack(5, 'C').getResult() == AtackStatus.HIT);
    }

    @Test
    public void testAttackHit() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'C', true);
        assertTrue(board.attack(5, 'C').getResult() == AtackStatus.HIT);
    }

    @Test
    public void testAttackSunk() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'C', true);
        board.placeShip(new Ship("DESTROYER"), 1, 'A', true);
        board.attack(5, 'C');
        assertTrue(board.attack(6, 'C').getResult() == AtackStatus.SUNK);
        board.attack(1, 'A');
        board.attack(2, 'A');
        assertTrue(board.attack(3, 'A').getResult() == AtackStatus.SURRENDER);

    }
}
