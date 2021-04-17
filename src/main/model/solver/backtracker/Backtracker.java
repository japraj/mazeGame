package model.solver.backtracker;

import model.maze.ImmutableMaze;
import model.path.Path;
import model.path.Position;
import model.solver.MazeSolver;

// Iterative backtracking search algorithm that terminates immediately upon finding a valid solution to the given maze
public class Backtracker extends MazeSolver {

    public Backtracker(ImmutableMaze maze) {
        super(maze);
    }

    @Override
    protected void init() {
        path = new BranchedPath();
    }

    @Override
    protected boolean isSolved() {
        Position tail = path.getTail();
        // check if final node in path is in the bottom right corner of the maze
        return tail.getPosX() == maze.getSize() - 2 && tail.getPosY() == maze.getSize() - 2;
    }

    // REQUIRES: Maze has not been fully solved yet
    // MODIFIES: this
    // EFFECTS: tick the algorithm one step forward and produce the current path
    @Override
    protected Path tick() {
        // adds branches to path object, using current branch as foundation
        ((BranchedPath) path).generateBranches(getValidMoves());
        ((BranchedPath) path).nextBranch();
        return path;
    }
}
