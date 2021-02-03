package model.solver;

import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.moveable.Move;
import model.path.Path;
import model.path.Position;

import java.util.ArrayList;
import java.util.List;

// An Algorithm that solves the given maze
public abstract class MazeSolver {

    public static final Move[] MOVES = {Move.UP, Move.DOWN, Move.LEFT, Move.RIGHT};
    protected ImmutableMaze maze;
    protected Path path;

    // REQUIRES: can only be called within constructor
    // MODIFIES: this
    // EFFECTS: set maze to be solved and add path that the algorithm should use as base for solution
    //          (empty path indicates that the algorithm should start from top left)
    public MazeSolver(ImmutableMaze maze, Path initPath) {
        this.maze = maze;
        path = initPath;
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

    private boolean isValid(Move move) {
        // we don't need to check for index out of bounds because if the maze is generated properly, then it will be
        // surrounded by WALL and therefore the user cannot ever get to a cell such that they are one Move away
        // from index out of bounds
        Position newPos = path.getTail().applyMove(move);
        return maze.getCell(newPos) == Maze.PATH && !path.containsNode(newPos);
    }

    protected List<Move> getValidMoves() {
        List<Move> moves = new ArrayList<>(2);
        for (Move move : MOVES) {
            if (isValid(move)) {
                moves.add(move);
            }
        }
        return moves;
    }
}
