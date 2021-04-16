package model;

import model.generator.MazeGenerator;
import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.path.Path;
import model.solver.backtracker.Backtracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class MazeGeneratorTest {

    private static final int MAX_TICKS = 2000;

    private MazeGenerator mazeGenerator;
    private ImmutableMaze maze;

    @BeforeEach
    public void setup() {
        mazeGenerator = new MazeGenerator(Maze.MIN_SIZE);
        maze = mazeGenerator.generateMaze();
    }

    @Test
    public void testUntouchedSurroundings() {
        for (int i = 0; i < Maze.MIN_SIZE; i++) {
            assertFalse(maze.isPath(i, 0));
            assertFalse(maze.isPath(0, i));
            assertFalse(maze.isPath(i, Maze.MIN_SIZE - 1));
            assertFalse(maze.isPath(Maze.MIN_SIZE - 1, i));
        }
    }

    @Test
    public void testPathWallRatio() {
        // make sure that the number of walls and paths are both > MazeGame.MIN_SIZE; this is to make sure that
        // the generation algorithm is in fact placing walls/paths (the size constraint is somewhat arbitrary
        // but we can be confident that both constraints will be met by any working maze-gen implementation
        // because area (# cells) increases quadratically w/ size)
        int pathCounter = 0;
        int wallCounter = 0;
        for (int i = 0; i < Maze.MIN_SIZE; i++) {
            for (int j = 0; j < Maze.MIN_SIZE; j++) {
                if (maze.isPath(i, j)) {
                    pathCounter++;
                } else {
                    wallCounter++;
                }
            }
        }
        assertTrue(pathCounter > Maze.MIN_SIZE);
        assertTrue(wallCounter > Maze.MIN_SIZE);
    }

    @Test
    public void testSingleSolve() {
        maze = mazeGenerator.generateMaze(Maze.MIN_SIZE * 2 - 1);
        Iterator<Path> solver = new Backtracker(maze).iterator();
        int ticks = 0;
        while (solver.hasNext() && ticks < MAX_TICKS) {
            solver.next();
            ticks++;
        }
        if (ticks == MAX_TICKS) {
            fail();
        }
    }

    @Test
    public void testMultipleSolve() {
        // test several times to make sure the generator doesn't just get lucky!
        Iterator<Path> solver;
        int ticks;
        int size = Maze.floorOdd(Maze.MIN_SIZE * 3);
        // large number to make sure it is given ample chance to solve - if the solver and maze generator both work
        // properly, the test's execution time will be much shorter
        for (int i = 0; i < 50; i++) {
            maze = mazeGenerator.generateMaze(size);
            solver = new Backtracker(maze).iterator();
            ticks = 0;
            while (solver.hasNext() && ticks < MAX_TICKS) {
                solver.next();
                ticks++;
            }
            // if took max # of ticks, then the solver was unable to solve; else, we implicitly return and the test is
            // passed
            if (ticks == MAX_TICKS) {
                fail();
            }
        }
    }

    @Test
    public void testBlankGeneration() {
        ImmutableMaze maze = MazeGenerator.generateBlankMaze(Maze.MIN_SIZE);
        for (int i = 0; i < Maze.MIN_SIZE; i++) {
            assertFalse(maze.isPath(i, 0));
            assertFalse(maze.isPath(0, i));
            assertFalse(maze.isPath(i, Maze.MIN_SIZE - 1));
            assertFalse(maze.isPath(0, Maze.MIN_SIZE - 1));
        }
        for (int y = 1; y < Maze.MIN_SIZE - 1; y++)
            for (int x = 1; x < Maze.MIN_SIZE - 1; x++)
                assertTrue(maze.isPath(x, y));
    }
}
