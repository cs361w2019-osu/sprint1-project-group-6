package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ResultTest {

    @Test
    public void testResultObjectCreate() {
        Square square = new Square(5, 'b');
        Result result = new Result();
        result.setLocation(square);
        result.setResult(AtackStatus.MISS);
        assertTrue(square.equals(result.getLocation()));
        assertTrue(AtackStatus.MISS == result.getResult());
    }
}
