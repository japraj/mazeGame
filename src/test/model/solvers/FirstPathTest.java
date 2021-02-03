package model.solvers;

import model.path.Path;
import model.solver.FirstPath;

public class FirstPathTest extends MazeSolverTest {

    public void init(Path path) {
        solver = new FirstPath(super.maze, path);
    }

}
