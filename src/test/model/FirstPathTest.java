package model;

import model.path.Path;
import model.solver.FirstPath;

public class FirstPathTest extends MazeSolverTest {

    public void init(Path path) {
        super.solver = new FirstPath(super.maze, new Path());
    }

    // TODO: write test to ensure that the first path is in fact used (dfs or bfs will determine which path is actually
    //       considered 'first')
}
