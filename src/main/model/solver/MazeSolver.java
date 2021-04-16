package model.solver;

import model.maze.ImmutableMaze;
import model.moveable.Move;
import model.path.Path;
import model.path.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// An Algorithm that solves the given maze
public abstract class MazeSolver implements Iterable<Path> {

    // The order of MOVES actually makes a significant difference to backtracking solvers; it determines what directions
    // we prioritize; we want DOWN and RIGHT to be prioritized because they are the direction of our goal!
    public static final Move[] MOVES = {Move.DOWN, Move.RIGHT, Move.UP, Move.LEFT};
    protected ImmutableMaze maze;
    protected Path path;

    // MODIFIES: this
    // EFFECTS: set maze to be solved
    public MazeSolver(ImmutableMaze maze) {
        this.maze = maze;
    }

    // REQUIRES: Maze has not been fully solved yet (only called if solved produces false)
    // MODIFIES: this
    // EFFECTS: tick the algorithm one step forward and produce the current path
    protected abstract Path tick();

    public Path getPath() {
        return path;
    }

    // EFFECTS: produces true if adding move to current path would not run us into a wall and would not take us to a
    //          cell that has already been visited
    private boolean isValid(Move move) {
        // we don't need to check for index out of bounds because if the maze is generated properly, then it will be
        // surrounded by WALL and therefore the user cannot ever get to a cell such that they are one Move away
        // from index out of bounds
        Position newPos = path.getTail().applyMove(move);
        return maze.isPath(newPos) && !path.containsNode(newPos);
    }

    // EFFECTS: produce a list of valid moves (that would not run us into a wall) from tail of current path
    protected List<Move> getValidMoves() {
        List<Move> moves = new ArrayList<>(2);
        for (Move move : MOVES) {
            if (isValid(move)) {
                moves.add(move);
            }
        }
        return moves;
    }

    // EFFECTS: resets field variables to solve maze from start
    protected abstract void init();

    // EFFECTS: produce iterator for this, reset path
    public Iterator<Path> iterator() {
        init();
        return new SolverIterator();
    }

    protected class SolverIterator implements Iterator<Path> {
        // EFFECTS: produce true if maze has not yet been solved
        @Override
        public boolean hasNext() {
            Position tail = path.getTail();
            // check if final node in path is in the bottom right corner of the maze
            return !(tail.getPosX() == maze.getSize() - 2 && tail.getPosY() == maze.getSize() - 2);
        }

        // EFFECTS: produce next path in solving algorithm
        @Override
        public Path next() {
            return tick();
        }
    }
}
