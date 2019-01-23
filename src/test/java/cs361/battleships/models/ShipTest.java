package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShipTest {

    @Test
    public void testShipDeath() {
        Ship shipMinesweeper = new Ship("MINESWEEPER");
        Ship shipDestroyer = new Ship("DESTROYER");
        Ship shipBattleship = new Ship("BATTLESHIP");

        shipMinesweeper.hitShip();
        assertFalse(shipMinesweeper.isDead());
        shipMinesweeper.hitShip();
        assertTrue(shipMinesweeper.isDead());

        shipDestroyer.hitShip();
        assertFalse(shipDestroyer.isDead());
        shipDestroyer.hitShip();
        assertFalse(shipDestroyer.isDead());
        shipDestroyer.hitShip();
        assertTrue(shipDestroyer.isDead());

        shipBattleship.hitShip();
        assertFalse(shipBattleship.isDead());
        shipBattleship.hitShip();
        assertFalse(shipBattleship.isDead());
        shipBattleship.hitShip();
        assertFalse(shipBattleship.isDead());
        shipBattleship.hitShip();
        assertTrue(shipBattleship.isDead());
    }
}
