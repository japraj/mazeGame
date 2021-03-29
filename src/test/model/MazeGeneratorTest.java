package model;

import model.generator.MazeGenerator;
import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.path.Path;
import model.solver.FirstPath;
import model.solver.MazeSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.controller.MazeGame;

import static org.junit.jupiter.api.Assertions.*;

public class MazeGeneratorTest {

    private static final int MAX_TICKS = 2000;

    private MazeGenerator mazeGenerator;
    private ImmutableMaze maze;

    @BeforeEach
    public void setup() {
        mazeGenerator = new MazeGenerator(MazeGame.MIN_SIZE);
        maze = mazeGenerator.generateMaze();
    }

    @Test
    public void testUntouchedSurroundings() {
        for (int i = 0; i < MazeGame.MIN_SIZE; i++) {
            assertEquals(Maze.WALL, maze.getCell(i, 0));
            assertEquals(Maze.WALL, maze.getCell(0, i));
            assertEquals(Maze.WALL, maze.getCell(i, MazeGame.MIN_SIZE - 1));
            assertEquals(Maze.WALL, maze.getCell(MazeGame.MIN_SIZE - 1, i));
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
        for (int i = 0; i < MazeGame.MIN_SIZE; i++) {
            for (int j = 0; j < MazeGame.MIN_SIZE; j++) {
                if (maze.getCell(i, j) == Maze.PATH) {
                    pathCounter++;
                } else {
                    wallCounter++;
                }
            }
        }
        assertTrue(pathCounter > MazeGame.MIN_SIZE);
        assertTrue(wallCounter > MazeGame.MIN_SIZE);
    }

    @Test
    public void testSingleSolve() {
        maze = mazeGenerator.generateMaze(MazeGame.MIN_SIZE * 2 - 1);
        MazeSolver solver = new FirstPath(maze, new Path());
        int ticks = 0;
        while (!solver.isSolved() && ticks < MAX_TICKS) {
            solver.tick();
            ticks++;
        }
        if (ticks == MAX_TICKS) {
            fail();
        }
    }

    @Test
    public void testMultipleSolve() {
        // test several times to make sure the generator doesn't just get lucky!
        MazeSolver solver;
        int ticks;
        int size = Maze.floorOdd((MazeGame.MIN_SIZE + MazeGame.MAX_SIZE) / 2);
        // large number to make sure it is given ample chance to solve - if the solver and maze generator both work
        // properly, the test's execution time will be much shorter
        for (int i = 0; i < 50; i++) {
            maze = mazeGenerator.generateMaze(size);
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
