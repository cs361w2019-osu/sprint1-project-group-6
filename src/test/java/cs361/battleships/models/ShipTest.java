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
        assertFalse(shipMinesweeper.isDead() == 1);
        shipMinesweeper.hitShip();
        assertTrue(shipMinesweeper.isDead() == 1);

        shipDestroyer.hitShip();
        assertFalse(shipDestroyer.isDead() == 1);
        shipDestroyer.hitShip();
        assertFalse(shipDestroyer.isDead() == 1);
        shipDestroyer.hitShip();
        assertTrue(shipDestroyer.isDead() == 1);

        shipBattleship.hitShip();
        assertFalse(shipBattleship.isDead() == 1);
        shipBattleship.hitShip();
        assertFalse(shipBattleship.isDead() == 1);
        shipBattleship.hitShip();
        assertFalse(shipBattleship.isDead() == 1);
        shipBattleship.hitShip();
        assertTrue(shipBattleship.isDead() == 1);
    }
}
