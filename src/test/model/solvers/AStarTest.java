package model.solvers;

import model.solver.AStar;

public class AStarTest extends MazeSolverTest {

    @Override
    public void init() {
        solver = new AStar(super.maze);
    }
}
