package model.solver;

import model.maze.ImmutableMaze;
import model.path.Path;

// Algorithm that terminates immediately upon finding a valid solution to the given maze
public class FirstPath extends MazeSolver {

    public FirstPath(ImmutableMaze maze) {
        super(maze);
    }

    // REQUIRES: Maze has not been fully solved yet
    // MODIFIES: this
    // EFFECTS: tick the algorithm one step forward and produce the current path
    @Override
    protected Path tick() {
        // adds branches to path object, using current branch as foundation
        path.generateBranches(getValidMoves());
        path.nextBranch();
        return path;
    }
}
