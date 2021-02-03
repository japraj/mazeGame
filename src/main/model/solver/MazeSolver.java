package model.solver;

import model.maze.ImmutableMaze;
import model.path.Path;
import model.path.Position;

// An Algorithm that solves the given maze
public abstract class MazeSolver {

    private ImmutableMaze maze;
    private Path path;

    // REQUIRES: can only be called within constructor
    // MODIFIES: this
    // EFFECTS: set maze to be solved and add path that the algorithm should use as base for solution
    //          (empty path indicates that the algorithm should start from top left)
    public MazeSolver(ImmutableMaze maze, Path initPath) {

    }

    // EFFECTS: produce true if maze has been solved
    public boolean isSolved() {
        Position tail = path.getTail();
        // check if final node in path is in the bottom right corner of the maze
        return tail.getPosX() == maze.getSize() - 2 && tail.getPosX() == tail.getPosY();
    }

    // REQUIRES: Maze has not been fully solved yet (only called if solved produces false)
    // MODIFIES: this
    // EFFECTS: tick the algorithm one step forward and produce the current path
    public abstract Path tick();

    public Path getPath() {
        return path;
    }
}
