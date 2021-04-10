package model;

import model.moveable.Move;
import model.path.Path;
import model.path.PathNode;
import model.path.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PathTest {

    private Path path;

    @BeforeEach
    public void setup() {
        path = new Path();
    }

    @Test
    public void testConstructor() {
        PathNode tail = path.getTail();
        assertEquals(new Position(1, 1), tail);
        assertNull(tail.getDirection());
    }

    @Test
    public void testAddNodePosition() {
        path.addNode(1, 2);
        path.addNode(2, 2);
        path.addNode(2, 1);
        path.addNode(1, 1);

        Iterator<PathNode> iter = path.iterator();

        assertTrue(iter.hasNext());
        PathNode node = iter.next();
        assertEquals(new Position(1, 1), node);
        assertNull(node.getDirection());

        assertTrue(iter.hasNext());
        node = iter.next();
        assertEquals(new Position(1, 2), node);
        assertEquals(Move.DOWN, node.getDirection());

        assertTrue(iter.hasNext());
        node = iter.next();
        assertEquals(new Position(2, 2), node);
        assertEquals(Move.RIGHT, node.getDirection());


        assertTrue(iter.hasNext());
        node = iter.next();
        assertEquals(new Position(2, 1), node);
        assertEquals(Move.UP, node.getDirection());

        assertTrue(iter.hasNext());
        node = iter.next();
        assertEquals(new Position(1, 1), node);
        assertEquals(Move.LEFT, node.getDirection());

        assertFalse(iter.hasNext());
    }

    @Test
    public void testAddNodeMove() {
        // populate path
        path.addNode(Move.DOWN);
        path.addNode(Move.RIGHT);
        path.addNode(Move.UP);
        path.addNode(Move.LEFT);


        Iterator<PathNode> iter = path.iterator();

        assertTrue(iter.hasNext());
        PathNode node = iter.next();
        assertEquals(new Position(1, 1), node);
        assertNull(node.getDirection());

        assertTrue(iter.hasNext());
        node = iter.next();
        assertEquals(new Position(1, 2), node);
        assertEquals(Move.DOWN, node.getDirection());

        assertTrue(iter.hasNext());
        node = iter.next();
        assertEquals(new Position(2, 2), node);
        assertEquals(Move.RIGHT, node.getDirection());


        assertTrue(iter.hasNext());
        node = iter.next();
        assertEquals(new Position(2, 1), node);
        assertEquals(Move.UP, node.getDirection());

        assertTrue(iter.hasNext());
        node = iter.next();
        assertEquals(new Position(1, 1), node);
        assertEquals(Move.LEFT, node.getDirection());

        assertFalse(iter.hasNext());
    }

    @Test
    public void testTailPop() {
        // populate path
        path.addNode(1, 2);
        path.addNode(1, 3);
        path.addNode(2, 3);

        // test single pop
        path.pop(1);
        // check tail
        assertEquals(new Position(1, 3), path.getTail());

        // test multiple pop
        path.pop(2);
        assertEquals(new Position(1, 1), path.getTail());
    }

    @Test
    public void testPopException() {
        try {
            path.pop(4);
            fail("Expected Exception");
        } catch (IllegalArgumentException e) {
            // make sure path was not mutated
            assertEquals(1, path.getLength());
            assertTrue(path.containsNode(1, 1));
        }
    }

    @Test
    public void testIteration() {
        List<Position> posList = new ArrayList<>();
        posList.add(new Position(1, 1));
        for (int i = 2; i <= 6; i++) {
            posList.add(new Position(1, i));
            path.addNode(1, i);
        }

        Iterator<PathNode> iter = path.iterator();

        for (Position p : posList) {
            assertEquals(p, iter.next());
        }
        assertFalse(iter.hasNext());
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

        // test forceAdd
        assertFalse(path.visitedNode(new Position(30, 30)));
        path.addVisited(new Position(30, 30));
        assertFalse(path.containsNode(new Position(30, 30)));
        assertTrue(path.visitedNode(new Position(30, 30)));
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
    public void testBranchingNoExceptions() {
        path.addNode(Move.DOWN);
        path.addNode(Move.RIGHT);
        path.addNode(Move.RIGHT);
        path.addNode(Move.RIGHT);
        path.generateBranches(Arrays.asList(Move.UP, Move.DOWN));
        path.nextBranch();
        path.generateBranches(Arrays.asList(Move.LEFT, Move.RIGHT));
        path.nextBranch();
        assertEquals(3, path.getTail().getPosX());
        assertEquals(1, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(5, path.getTail().getPosX());
        assertEquals(1, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(4, path.getTail().getPosX());
        assertEquals(3, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(4, path.getTail().getPosX());
        assertEquals(2, path.getTail().getPosY());
        path.nextBranch();
    }

    @Test
    public void testBranchingExceptions() {
        path.addNode(Move.DOWN);
        // 1st move in list is invalid
        try {
            path.generateBranches(Arrays.asList(Move.UP));
            fail("Expected Exception");
        } catch (IllegalArgumentException e) {
            assertEquals(2, path.getLength());
        }
        path.addNode(Move.RIGHT);
        path.addNode(Move.DOWN);
        path.addNode(Move.LEFT);

        // 3rd Move in list is invalid
        try {
            path.generateBranches(Arrays.asList(Move.LEFT, Move.DOWN, Move.UP));
            fail("Expected Exception");
        } catch (IllegalArgumentException e) {
            assertEquals(5, path.getLength());
        }
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
        // Note: no try catch because IllegalArgument is a RuntimeException
        assertTrue(path.containsNode(1, 1));
        PathNode node = path.getNode(1, 1);
        assertNull(node.getDirection());
        assertTrue(node.equals(1, 1));
    }

    @Test
    public void testGetNodeException() {
        for (int i = 2; i < 10; i++) {
            for (int j = 2; j < 10; j++) {
                try {
                    path.getNode(i, j);
                    fail("Expected Exception");
                } catch (IllegalArgumentException e) {
                    // let test pass
                }
            }
        }
    }

    @Test
    public void testSubtractionCommonOriginDirection() {
        Path pathA = new Path();
        pathA.addNode(Move.DOWN);
        pathA.addNode(Move.DOWN);
        pathA.addNode(Move.DOWN);
        pathA.addNode(Move.DOWN);

        Path pathB = new Path();
        pathB.addNode(Move.DOWN);
        pathB.addNode(Move.DOWN);
        pathB.addNode(Move.RIGHT);
        pathB.addNode(Move.DOWN);
        pathB.addNode(Move.DOWN);

        // this is a call to the static method
        List<PathNode> difference1 = Path.subtract(pathA.getNodes(), pathB.getNodes(), false);
        assertEquals(2, difference1.size());
        assertTrue(difference1.contains(new PathNode(1, 4, Move.DOWN)));
        assertTrue(difference1.contains(new PathNode(1, 5, Move.DOWN)));

        // call to the non-static overload
        List<PathNode> difference2 = pathB.subtract(pathA.getNodes(), false);
        assertEquals(3, difference2.size());
        assertTrue(difference2.contains(new PathNode(2, 3, Move.RIGHT)));
        assertTrue(difference2.contains(new PathNode(2, 4, Move.DOWN)));
        assertTrue(difference2.contains(new PathNode(2, 5, Move.DOWN)));

        List<PathNode> difference3 = pathB.subtract(pathA.getNodes(), true);
        assertEquals(4, difference3.size());
        for (PathNode p : difference2) {
            assertTrue(difference3.contains(p));
        }
        assertTrue(difference3.contains(new PathNode(1, 3, Move.DOWN)));
    }

    @Test
    public void testSubtractionUniqueOriginDirection() {
        Path pathA = new Path();
        pathA.addNode(Move.DOWN);

        Path pathB = new Path();
        pathB.addNode(Move.RIGHT);
        pathB.addNode(Move.DOWN);
        pathB.addNode(Move.LEFT);

        List<PathNode> difference1 = pathA.subtract(pathB.getNodes(), true);
        assertEquals(1, difference1.size());
        assertEquals(new PathNode(1, 2, Move.DOWN), difference1.get(0));

        List<PathNode> difference2 = pathB.subtract(pathA.getNodes(), true);
        assertEquals(3, difference2.size());
        assertTrue(difference2.contains(new PathNode(2, 1, Move.RIGHT)));
        assertTrue(difference2.contains(new PathNode(2, 2, Move.DOWN)));
        assertTrue(difference2.contains(new PathNode(1, 2, Move.LEFT)));
        assertEquals(difference2, pathB.subtract(pathA.getNodes(), false));
    }

    @Test
    public void testSubtractionSymmetry() {
        Path path = new Path();
        for (int i = 0; i < 10; i++) {
            path.addNode(Move.DOWN);
        }
        assertTrue(path.subtract(path.getNodes(), false).isEmpty());
    }

}
