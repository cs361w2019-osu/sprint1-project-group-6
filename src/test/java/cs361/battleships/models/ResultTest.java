package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

public class ResultTest {

    @Test
    public void testResultObjectCreate() {
        Square square = new Square(5, 'b');
        Result result = new Result();
        result.setLocation(square);
        result.setResult(AtackStatus.MISS);
        assertSame(square,result.getLocation());
        assertEquals(AtackStatus.MISS,result.getResult());
    }
}
