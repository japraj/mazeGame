package model.solver;

import model.maze.ImmutableMaze;
import model.moveable.Move;
import model.path.Path;
import model.path.Position;

import java.util.LinkedList;
import java.util.List;

// Algorithm that terminates immediately upon finding a valid solution to the given maze
public class FirstPath extends MazeSolver {

    private List<List<Move>> workList;

    public FirstPath(ImmutableMaze maze, Path path) {
        super(maze, path);
        workList = new LinkedList<>();
    }

    // REQUIRES: Maze has not been fully solved yet (only called if solved produces false)
    // MODIFIES: this
    // EFFECTS: tick the algorithm one step forward and produce the current path
    @Override
    public Path tick() {
        if (isSolved()) {
            return path;
        }
        path.generateBranches(getValidMoves()); // adds branches to path object, using current branch as foundation
        path.nextBranch(); // moves to next branch
        return path;
    }
}
