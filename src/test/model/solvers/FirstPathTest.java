package model.solvers;

import model.solver.FirstPath;

public class FirstPathTest extends MazeSolverTest {

    public void init() {
        solver = new FirstPath(super.maze);
    }

}
