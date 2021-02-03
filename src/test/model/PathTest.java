package model;

import model.moveable.Move;
import model.path.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathTest {

    private Path path;

    @BeforeEach
    public void setup() {
        path = new Path();
    }

    @Test
    public void testConstructor() {
        assertEquals(1, path.getPosition().getPosX());
        assertEquals(1, path.getPosition().getPosY());
        assertNull(path.getDirection());
    }

    @Test
    public void testAddNode() {
        path.addNode(1, 2);
        path.addNode(2, 2);
        path.addNode(2, 1);
        path.addNode(1, 1);

        assertTrue(path.next());
        assertEquals(1, path.getPosition().getPosX());
        assertEquals(2, path.getPosition().getPosY());
        assertEquals(Move.DOWN, path.getDirection());

        assertTrue(path.next());
        assertEquals(2, path.getPosition().getPosX());
        assertEquals(2, path.getPosition().getPosY());
        assertEquals(Move.RIGHT, path.getDirection());


        assertTrue(path.next());
        assertEquals(2, path.getPosition().getPosX());
        assertEquals(1, path.getPosition().getPosY());
        assertEquals(Move.UP, path.getDirection());

        assertFalse(path.next());
        assertEquals(1, path.getPosition().getPosX());
        assertEquals(1, path.getPosition().getPosY());
        assertEquals(Move.LEFT, path.getDirection());
    }

    @Test
    public void testTail() {
        // populate path
        path.addNode(1, 2);
        path.addNode(1, 3);
        path.addNode(2, 3);

        // check tail
        assertEquals(2, path.getTail().getPosX());
        assertEquals(3, path.getTail().getPosY());

        // move index
        assertTrue(path.next());
        assertTrue(path.next());
        assertFalse(path.next());

        // check tail
        assertEquals(path.getPosition(), path.getTail());

        // add node and check tail
        path.addNode(2, 4);
        assertEquals(2, path.getTail().getPosX());
        assertEquals(4, path.getTail().getPosY());
    }

    @Test
    public void testIteration() {
        path.addNode(1, 2);
        path.addNode(1, 3);
        path.addNode(1, 4);
        path.addNode(1, 5);
        path.addNode(1, 6);

        int i = 2;
        while(path.next()) {
            assertEquals(1, path.getPosition().getPosX());
            assertEquals(i, path.getPosition().getPosY());
            if (i == 1) {
                assertNull(path.getDirection());
            } else {
                assertEquals(Move.DOWN, path.getDirection());
            }
            i++;
        }
        assertEquals(6, i);

        path.reset();

        i = 2;
        while(path.next()) {
            assertEquals(1, path.getPosition().getPosX());
            assertEquals(i, path.getPosition().getPosY());
            if (i == 1) {
                assertNull(path.getDirection());
            } else {
                assertEquals(Move.DOWN, path.getDirection());
            }
            i++;
        }
        assertEquals(6, i);
    }

    @Test
    public void testEqualsTrue() {
        Path comparePath = new Path();
        // RIGHT
        for (int i = 2; i < 10; i++) {
            path.addNode(1, i);
            comparePath.addNode(1, i);
        }
        // DOWN
        for (int i = 2; i < 5; i++) {
            path.addNode(i, 9);
            comparePath.addNode(i, 9);
        }
        // LEFT
        path.addNode(4, 8);
        comparePath.addNode(4, 8);
        // UP
        path.addNode(3, 8);
        comparePath.addNode(3, 8);

        assertTrue(path.equals(comparePath));
        assertTrue(comparePath.equals(path));
    }

    @Test
    public void testEqualsFalse() {
        Path comparePath = new Path();
        for (int i = 2; i < 10; i++) {
            path.addNode(1, i);
            comparePath.addNode(i, 1);
        }
        for (int i = 2; i < 5; i++) {
            path.addNode(i, 9);
            comparePath.addNode(9, i);
        }

        assertFalse(path.equals(comparePath));
        assertFalse(comparePath.equals(path));

    }
}
