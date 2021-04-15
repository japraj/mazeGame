package model.generator;

import model.maze.Maze;
import model.moveable.Move;
import model.path.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Stream;

// Generates randomized Mazes of arbitrary size
public class MazeGenerator {

    public static final Position INIT = new Position(1, 1);

    private int size;
    private Maze maze;
    private Random random;
    private List<Position> visited;
    private Stack<Position> stack;

    // REQUIRES: size must be odd and in [MazeGame.MIN_SIZE, MazeGame.MAX_SIZE]
    // EFFECTS: initialize maze
    public MazeGenerator(int size) {
        this.size = size;
        random = new Random();
    }

    // MODIFIES: this
    // EFFECTS: initialize the local variables to set up graph for generation algorithm
    private void init() {
        maze = new Maze(size);
        visited = new ArrayList<>((int) Math.pow(size - 2, 2));
        stack = new Stack<>();

        stack.push(INIT);
        visited.add(INIT);
        maze.setCell(INIT, Maze.PATH);

        // set all vertices to PATH
        for (int x = 1; x < size - 1; x += 2) {
            for (int y = 1; y < size - 1; y += 2) {
                maze.setCell(x, y, Maze.PATH);
            }
        }
    }

    // REQUIRES: size must be odd and in [MazeGame.MIN_SIZE, MazeGame.MAX_SIZE]
    // MODIFIES: this
    // EFFECTS: produce a square maze with specified side-length
    public Maze generateMaze(int size) {
        this.size = size;
        return generateMaze();
    }

    // MODIFIES: this
    // EFFECTS: produce a square maze with most recently specified side-length
    public Maze generateMaze() {
        // This is an iterative implementation of the randomized depth-first search maze generation algorithm from
        // Wikipedia: https://en.wikipedia.org/wiki/Maze_generation_algorithm
        // We model a graph with the 2-dimensional boolean array that is Maze so we can apply algorithms that operate on
        // graphs to generate a spanning tree
        init();
        Position[] unvisitedNeighbours;
        Position current;
        Position neighbour;

        while (!stack.empty()) {
            current = stack.pop();
            unvisitedNeighbours = getValidNeighbours(current)
                    .filter(p -> !visited.contains(p))
                    .toArray(Position[]::new);

            if (unvisitedNeighbours.length > 0) {
                stack.push(current);
                neighbour = unvisitedNeighbours[random.nextInt(unvisitedNeighbours.length)];
                maze.setCell(getEdge(current, neighbour), Maze.PATH);
                visited.add(neighbour);
                stack.push(neighbour);
            }
        }

        return maze;
    }

    // EFFECTS: produces a stream of valid Positions from pos (x and y are both in the range [1, size - 2]); a neighbour
    //          is not an adjacent cell, but rather, is the closest vertex in the graph that the Maze models. The
    //          closest vertex in a particular direction is always 2 steps away (if abc are cells, then a and b are
    //          neighbouring vertices)
    private Stream<Position> getValidNeighbours(Position pos) {
        return Stream.of(Move.UP, Move.DOWN, Move.LEFT, Move.RIGHT)
                .map(m -> pos.applyMove(m).applyMove(m))
                .filter(p -> isValid(p.getPosX()) && isValid(p.getPosY()));
    }

    // EFFECTS: produce true if coord is in range [1, size - 2]
    private boolean isValid(int coord) {
        return 0 < coord && coord < size - 1;
    }

    // REQUIRES: origin and destination must be neighbouring vertices, meaning that they have exactly one cell between
    //           them and are either horizontally or vertically aligned
    // EFFECTS: produces the position of the cell that is between origin and destination
    private Position getEdge(Position origin, Position destination) {
        if (origin.getPosY() == destination.getPosY()) {
            return origin.applyMove(origin.getPosX() - destination.getPosX() < 0 ? Move.RIGHT : Move.LEFT);
        } else {
            return origin.applyMove(origin.getPosY() - destination.getPosY() < 0 ? Move.DOWN : Move.UP);
        }
    }

    // EFFECTS: produce a new maze with WALL border and PATH in all cells that are not on the border
    public static Maze generateBlankMaze(int size) {
        Maze maze = new Maze(size);
        for (int y = 1; y < size - 1; y++)
            for (int x = 1; x < size - 1; x++)
                maze.setCell(x, y, Maze.PATH);
        return maze;
    }

}
