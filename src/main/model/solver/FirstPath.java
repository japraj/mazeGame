package model.solver;

import model.maze.ImmutableMaze;
import model.path.Path;

// Algorithm that terminates immediately upon finding a valid solution to the given maze
public class FirstPath extends MazeSolver {

    public FirstPath(ImmutableMaze maze, Path path) {
        super(maze, path);
    }

    // REQUIRES: Maze has not been fully solved yet (only called if solved produces false)
    // MODIFIES: this
    // EFFECTS: tick the algorithm one step forward and produce the current path
    @Override
    public Path tick() {
        return null;
    }
}