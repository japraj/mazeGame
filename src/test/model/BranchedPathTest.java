package model;

import model.moveable.Move;
import model.path.Position;
import model.solver.backtracker.BranchedPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BranchedPathTest {

    private BranchedPath path;

    @BeforeEach
    public void setup() {
        path = new BranchedPath();
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
    public void testBranchingNoExceptions() {
        path.addNode(Move.DOWN);
        path.addNode(Move.RIGHT);
        path.addNode(Move.RIGHT);
        path.addNode(Move.RIGHT);
        path.generateBranches(Arrays.asList(Move.UP, Move.DOWN));
        path.nextBranch();
        path.generateBranches(Arrays.asList(Move.LEFT, Move.RIGHT));
        path.nextBranch();
        assertEquals(5, path.getTail().getPosX());
        assertEquals(3, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(3, path.getTail().getPosX());
        assertEquals(3, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(4, path.getTail().getPosX());
        assertEquals(1, path.getTail().getPosY());
        path.nextBranch();
        assertEquals(4, path.getTail().getPosX());
        assertEquals(2, path.getTail().getPosY());
        path.nextBranch();
    }

}
