package model;

import model.generator.MazeGenerator;
import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.path.Path;
import model.solver.FirstPath;
import model.solver.MazeSolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MazeGeneratorTest {

    private ImmutableMaze maze;

    // no BeforeEach because the ref is immutable, meaning we do not need to re-initialize it every time
    public void setup() {
        maze = MazeGenerator.generateMaze(Maze.MIN_SIZE);
    }

    @Test
    public void testUntouchedSurroundings() {
        for (int i = 0; i < Maze.MIN_SIZE; i++) {
            assertEquals(Maze.WALL, maze.getCell(i, 0));
            assertEquals(Maze.WALL, maze.getCell(0, i));
            assertEquals(Maze.WALL, maze.getCell(i, Maze.MIN_SIZE - 1));
            assertEquals(Maze.WALL, maze.getCell(Maze.MIN_SIZE - 1, i));
        }
    }

    @Test
    public void testPathWallRatio() {
        // make sure that the number of walls and paths are both > Maze.MIN_SIZE; this is to make sure that
        // the generation algorithm is in fact placing walls/paths (the size constraint is somewhat arbitrary
        // but we can be confident that both constraints will be met by any working maze-gen implementation
        // because area (# cells) increases quadratically w/ size)
        int pathCounter = 0;
        int wallCounter = 0;
        for (int i = 0; i < Maze.MIN_SIZE; i++) {
            for (int j = 0; j < Maze.MIN_SIZE; j++) {
                if (maze.getCell(i, j) == Maze.PATH) {
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
    public void testSolveable() {
        // test 10 times to make sure the generator doesn't just get lucky!
        MazeSolver solver;
        int ticks;
        // large number to make sure it is given ample chance to solve - if the solver and maze generator both work
        // properly, the test's execution time will be much shorter
        final int MAX_TICKS = 2000;
        for (int i = 0; i < 10; i++) {
            maze = MazeGenerator.generateMaze((Maze.MIN_SIZE + Maze.MAX_SIZE) / 2); // arbitrary size choice
            solver = new FirstPath(maze, new Path());
            ticks = 0;
            while (!solver.isSolved() && ticks < MAX_TICKS) {
                solver.tick();
                ticks++;
            }
            // if took max # of ticks, then the solver was unable to solve; else, we implicitly return and the test is
            // passed
            if (ticks == MAX_TICKS) {
                fail();
            }
        }
    }
}
