package model;

import model.moveable.Move;
import model.path.Path;
import model.path.PathNode;
import model.path.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
    public void testAddNodePosition() {
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
    public void testAddNodeMove() {
        // populate path
        path.addNode(Move.RIGHT);
        path.addNode(Move.DOWN);
        path.addNode(Move.LEFT);
        path.addNode(Move.UP);

        path.next();
        assertEquals(2, path.getPosition().getPosX());
        assertEquals(1, path.getPosition().getPosY());
        assertEquals(Move.RIGHT, path.getDirection());

        path.next();
        assertEquals(2, path.getPosition().getPosX());
        assertEquals(2, path.getPosition().getPosY());
        assertEquals(Move.DOWN, path.getDirection());

        path.next();
        assertEquals(1, path.getPosition().getPosX());
        assertEquals(2, path.getPosition().getPosY());
        assertEquals(Move.LEFT, path.getDirection());

        path.next();
        assertEquals(1, path.getPosition().getPosX());
        assertEquals(1, path.getPosition().getPosY());
        assertEquals(Move.UP, path.getDirection());
    }

    @Test
    public void testTailPop() {
        // populate path
        path.addNode(1, 2);
        path.addNode(1, 3);
        path.addNode(2, 3);

        // check tail
        assertEquals(3, path.getTail().getPosY());
        assertEquals(2, path.getTail().getPosX());

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

        // test single pop
        path.pop(1);
        // check tail
        assertEquals(3, path.getTail().getPosY());
        assertEquals(2, path.getTail().getPosX());

        // test multiple pop
        path.pop(3);
        assertEquals(1, path.getTail().getPosY());
        assertEquals(1, path.getTail().getPosX());
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

        assertEquals(path, comparePath);
        assertEquals(comparePath, path);
    }

    @Test
    public void testVisitedContains() {
        // correct init
        assertTrue(path.containsNode(new Position(1, 1)));
        assertTrue(path.visitedNode(new Position(1, 1)));

        // add node
        path.addNode(Move.RIGHT);
        assertTrue(path.containsNode(new Position(2, 1)));
        assertTrue(path.visitedNode(new Position(2, 1)));

        // remove node
        path.pop(1);
        assertFalse(path.containsNode(new Position(2, 1)));
        assertTrue(path.visitedNode(new Position(2, 1)));

        // test unseen node
        assertFalse(path.containsNode(new Position(3, 5)));
        assertFalse(path.visitedNode(new Position(3, 5)));
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
        // same length, different tails
        assertNotEquals(path, comparePath);
        assertNotEquals(comparePath, path);

        // give them the same tail
        path.addNode(10, 10);
        comparePath.addNode(10, 10);

        // check failure due to different x coords - two assertions to ensure symmetry
        assertNotEquals(path, comparePath);
        assertNotEquals(comparePath, path);

        // check different lengths but same tail
        path.addNode(10, 10);
        assertNotEquals(path, comparePath);

        // check different lengths different tail
        path.addNode(Move.DOWN);
        assertNotEquals(path, comparePath);

        // random obj that is not instanceof path to make sure it fails
        assertNotEquals(path, "");

        // same length, same tail, difference is in y coord
        path = new Path();
        comparePath = new Path();
        path.addNode(3, 4);
        comparePath.addNode(3, 5);
        path.addNode(4, 4);
        comparePath.addNode(4, 4);
        assertNotEquals(path, comparePath);
    }

    @Test
    public void testBranching() {
        path.addNode(3, 4);
        path.generateBranches(Arrays.asList(Move.UP, Move.DOWN, Move.LEFT, Move.RIGHT));
        path.nextBranch();
        path.generateBranches(Arrays.asList(Move.UP, Move.DOWN, Move.LEFT, Move.RIGHT));
        path.nextBranch();
        assertEquals(3, path.getTail().getPosX());
        assertEquals(2, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(3, path.getTail().getPosX());
        assertEquals(4, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(2, path.getTail().getPosX());
        assertEquals(3, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(4, path.getTail().getPosX());
        assertEquals(3, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(3, path.getTail().getPosX());
        assertEquals(5, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(2, path.getTail().getPosX());
        assertEquals(4, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(4, path.getTail().getPosX());
        assertEquals(4, path.getTail().getPosY());
        path.nextBranch();
    }

    @Test
    public void testNoDuplicates() {
        path = new Path(true);
        // try adding duplicates
        path.addNode(Move.DOWN);
        path.addNode(Move.UP);
        path.addNode(1, 2);
        // only nodes in the path should be 1,1 and 1,2
        assertEquals(2, path.getLength());
    }

    @Test
    public void testGetNode() {
        assertTrue(path.containsNode(1, 1));
        PathNode node = path.getNode(1, 1);
        assertNull(node.getDirection());
        assertTrue(node.equals(1, 1));
    }
}
