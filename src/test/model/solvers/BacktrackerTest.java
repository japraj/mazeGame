package model.solvers;

import model.solver.backtracker.Backtracker;

public class BacktrackerTest extends MazeSolverTest {

    public void init() {
        solver = new Backtracker(super.maze);
    }

}
