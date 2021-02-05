package model;

import model.path.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testEqualsObject() {
        Position pos = new Position(2, 3);
        // not instanceof
        assertNotEquals(pos, 5);
        // both coords unequal
        assertNotEquals(pos, new Position(1, 1));
        // x unequal
        assertNotEquals(pos, new Position(1, 3));
        // y unequal
        assertNotEquals(pos, new Position(2, 1));
        // both equal
        assertEquals(pos, new Position(2, 3));
    }

    @Test
    public void testEqualsCoords() {
        Position pos = new Position(2, 3);
        // both coords unequal
        assertFalse(pos.equals(1, 1));
        // x unequal
        assertFalse(pos.equals(1, 3));
        // y unequal
        assertFalse(pos.equals(2, 1));
        // both equal
        assertTrue(pos.equals(2, 3));
    }

}
