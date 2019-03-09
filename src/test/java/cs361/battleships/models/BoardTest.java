package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testSpaceLaserInBound() {
        Board board = new Board();
        Result res = board.spaceLaser(1, 'A');
        assertFalse(board.spaceLaserInBound(res));
        Result res2 = board.spaceLaser(-1, 'A');
        assertTrue(board.spaceLaserInBound(res2));
    }

    @Test
    public void testSpaceLaser() {
        Board board = new Board();
        board.spaceLaser(1, 'A');
        assertTrue(board.spaceLaser(1, 'A').getResult() == null);
    }

    @Test
    public void testGetAttacks() {
        Board board = new Board();
        Result res = new Result();
        res.setLocation(new Square(5, 'Q'));
        assertTrue(board.getAttacks() != null);
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
        assertFalse(board.placeShip(new Ship("DESTROYER"), 5, 'C', true));
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
        assertNotNull(board.attack(5, 'C').getResult() == AtackStatus.HIT);
    }

    @Test
    public void testAttackHit() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'C', true);
        assertNotNull(board.attack(5, 'C').getResult());
    }

    @Test
    public void testAttackSunk() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 5, 'C', true);
        board.placeShip(new Ship("DESTROYER"), 1, 'A', true);
        board.attack(5, 'C');
        assertTrue(board.attack(6, 'C').getResult() == AtackStatus.SUNK);
    }

    @Test
    public void testPulseOutOfBounds() {
        Board board = new Board();
        Result res = new Result();
        res.setLocation(new Square(5, 'Q'));
        boolean result = board.pulsedOutOfBounds(res);
        assertTrue(result);
    }

    @Test
    public void testPulse() {
        Board board = new Board();
        board.pulse(1, 'A');
        assertTrue(board.pulsed.size() == 6);
    }

    @Test
    public void testMoveEdge() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', false);
        board.moveShip(0);
        assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getRow() == 1);
        assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getColumn() == 'A');

        board.moveShip(1);
        assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getRow() == 1);
        assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getColumn() == 'A');

        board.moveShip(3);
        assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getRow() == 1);
        assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getColumn() == 'B');

        board.moveShip(2);
        assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getRow() == 2);
        assertTrue(board.getShips().get(0).getOccupiedSquares().get(0).getColumn() == 'B');
    }
}
