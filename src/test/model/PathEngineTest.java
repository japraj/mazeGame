package model;

import model.moveable.Move;
import model.path.Path;
import model.path.PathEngine;
import model.path.PathNode;
import model.path.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathEngineTest {

    private PathEngine engine;

    @BeforeEach
    public void setup() {
        engine = new PathEngine();
    }

    @Test
    public void testAddWipeComplexPath() {
        // create two paths; their deepest common node has index 1
        Path branch1 = new Path();
        branch1.addNode(Move.DOWN);
        branch1.addNode(Move.DOWN);
        branch1.addNode(Move.DOWN);
        branch1.addNode(Move.DOWN);

        Path branch2 = new Path();
        branch2.addNode(Move.DOWN);
        branch2.addNode(Move.DOWN);
        branch2.addNode(Move.RIGHT);
        branch2.addNode(Move.RIGHT);

        // add first branch to the engine; toWipe should be initialized but empty, and toPlace should have 5 elements
        // because none of the elements in branch1 are already present (note that the 5th element is the node 1,1)
        engine.addPath(branch1);
        List<PathNode> toWipe = engine.getToWipe();
        List<PathNode> toPlace = engine.getToPlace();
        assertTrue(toWipe.isEmpty());
        assertEquals(5, toPlace.size());
        for (PathNode p : branch1) {
            assertTrue(toPlace.contains(p));
        }

        // add second branch to the engine; we want that the engine tells us to wipe 2 nodes, and that it tells us to
        // paint 3 nodes (repaint head)
        engine.addPath(branch2);

        toWipe = engine.getToWipe();
        toPlace = engine.getToPlace();
        assertEquals(2, toWipe.size());
        assertEquals(3, toPlace.size());

        assertTrue(toWipe.contains(new Position(1, 4)));
        assertTrue(toWipe.contains(new Position(1, 5)));

        assertTrue(toPlace.contains(new Position(1, 3)));
        assertTrue(toPlace.contains(new Position(2, 3)));
        assertTrue(toPlace.contains(new Position(3, 3)));

        List<PathNode> placed = engine.getPlaced();
        assertEquals(6, placed.size());
        for (PathNode p : branch2.getNodes()) {
            assertTrue(placed.contains(p));
        }
    }
}
