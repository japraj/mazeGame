package model;

import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.path.Path;
import model.solver.MazeSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class MazeSolverTest {
    // This test suite is intended to provide a baseline for all MazeSolver implementations - it only tests that the
    // given implementation is in fact capable of solving mazes; it does not test whether other conditions are
    // satisfied (ex. does the solver find the first or shortest path?). Additional tests are necessary! All tests are
    // written to use mazes that have only a single solution so it works for all MazeSolver implementations.

    // Usage: implement the init method (do not override setup; init is used to allow for testing with non-null initial
    // paths)

    protected MazeSolver solver;
    // not immutable because we want to test it directly
    protected Maze maze;

    @BeforeEach
    public void setup() {
        maze = new Maze(Maze.MIN_SIZE);
    }


    // initialize the solver variable with the algorithm that is being tested, using super.maze and the given path param
    // as arguments for the constructor
    abstract public void init(Path path);

    public void init() {
        init(new Path());
    }

    @Test
    public void testRandomInitPath() {
        // this test checks the solver's ability solve from any given starting point (the test below tests with 1,1
        // as the starting point)

        // initialize a maze that has a single solution starting at 1,1 going all the way down, and the going all the
        // way to the right (like an L shape), and build up a solution path for comparison simultaneously
        Path solution = new Path();
        for (int y = 1; y < Maze.MIN_SIZE - 1; y++) {
            maze.setCell(1, y, Maze.PATH);
            solution.addNode(1, y);
        }
        for (int x = 1; x < Maze.MIN_SIZE - 1; x++) {
            maze.setCell(x, Maze.MIN_SIZE - 2, Maze.PATH);
            solution.addNode(x, Maze.MIN_SIZE - 2);
        }

        Path initPath = new Path();
        initPath.addNode(2, 1);
        initPath.addNode(3, 1);
        initPath.addNode(4, 1);
        init(initPath);

        // tick the solver to completion - if it takes more than 50 ticks, it is in an infinite loop (maze is trivial)
        int ticks = 0;
        while (!solver.isSolved() && ticks < 50) {
            solver.tick();
            ticks++;
        }
        assertTrue(solution.equals(solver.getPath()));
    }

    @Test
    public void testSolveBacktracking() {
        /*
        *******
        *     *
        * *** *
        * *** *
        * *** *
        *     *
        *******

        this test uses 2 mazes; both are a variation of the one above - one blocks off the lower path, the other blocks
        off the upper path. most solver implementations will have to use backtracking to solve one of them
         */
        Path solutionLower = new Path();
        Path solutionUpper = new Path();

        // init the maze to look like above, and build the solution paths (need 2 loops so we can sequentially build the
        // Path variables - the maze itself could actually be done with a single loop)
        for (int i = 1; i < Maze.MIN_SIZE - 1; i++) {
            maze.setCell(1, i, Maze.PATH);
            solutionLower.addNode(1, i);

            maze.setCell(i, 1, Maze.PATH);
            solutionUpper.addNode(i, 1);
        }
        for (int i = 1; i < Maze.MIN_SIZE - 1; i++) {
            maze.setCell(i, Maze.MIN_SIZE - 2, Maze.PATH);
            solutionLower.addNode(i, Maze.MIN_SIZE - 2);

            maze.setCell(Maze.MIN_SIZE - 2, i, Maze.PATH);
            solutionUpper.addNode(Maze.MIN_SIZE - 2, i);
        }

        // block the bottom path, so the upper path is the only valid solution
        int blockCoord = Maze.MIN_SIZE - 3;
        maze.setCell(blockCoord, Maze.MIN_SIZE - 2, Maze.WALL);

        init();

        // tick the solver to completion - if it takes more than 150 ticks, it is in an infinite loop (maze is trivial)
        int ticks = 0;
        while (!solver.isSolved() && ticks < 150) {
            solver.tick();
            ticks++;
        }
        assertTrue(solutionUpper.equals(solver.getPath()));

        // reset solver, block the upper path, and unblock the lower path, so the lower path is the only valid solution
        init();
        maze.setCell(blockCoord, Maze.MIN_SIZE - 2, Maze.PATH);
        maze.setCell(Maze.MIN_SIZE - 2, blockCoord, Maze.WALL);

        // tick the solver to completion - if it takes more than 150 ticks, it is in an infinite loop (maze is trivial)
        ticks = 0;
        while (!solver.isSolved() && ticks < 150) {
            solver.tick();
            ticks++;
        }
        assertTrue(solutionUpper.equals(solver.getPath()));
    }

    @Test
    public void testSolveWithJoins() {
        /*
        *******
        *   * *
        * * * *
        *     *
        *** * *
        *     *
        *******

        this maze has joins (loops) - tests to make sure that the solver does not get stuck forever!
         */

        // clear all walls (except surrounding walls)
        for (int y = 1; y < Maze.MIN_SIZE - 1; y++) {
            for (int x = 1; x < Maze.MIN_SIZE - 1; x++) {
                maze.setCell(x, y, Maze.PATH);
            }
        }
        // manually place walls so it conforms to picture above
        maze.setCell(2, 2, Maze.WALL);
        maze.setCell(1, 4, Maze.WALL);
        maze.setCell(2, 4, Maze.WALL);
        maze.setCell(4, 1, Maze.WALL);
        maze.setCell(4, 2, Maze.WALL);
        maze.setCell(4, 4, Maze.WALL);

        // tick the solver to completion - if it takes more than 150 ticks, it is in an infinite loop
        int ticks = 0;
        while (!solver.isSolved() && ticks < 150) {
            solver.tick();
            ticks++;
        }

        // if it is 149 ticks, the solver failed (should not even need 100)
        if (ticks == 149) {
            fail();
        }
        // all solutions to this maze are 9 nodes long (due to the symmetry); there are 4 solutions so we simply check
        // path length - if all other tests are passed, then this one will not have an invalid solution
        assertEquals(9, solver.getPath().length());
    }

}