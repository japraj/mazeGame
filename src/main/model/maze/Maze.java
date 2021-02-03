package model.maze;

// A square Maze; each cell is a boolean (true and false represent PATH and WALL, respectively). A maze is started at
// the top left, at position (1, 1) and terminate in the bottom right, at position (size - 2, size - 2); the maze
// does in fact use zero-based indexing, but all Mazes are surrounded by wall (i.e. the top and bottom rows, and
// left-most and right-most columns are all WALL). All mazes must be solveable starting at the top left. This means
// that all mazes have PATH at positions (1, 1) and (size - 2, size - 2), and there is some way to get from the init
// position to the termination position.
public class Maze implements ImmutableMaze {

    // these static variables should be used in the stead of magic constants - they represent how a cell of the Maze
    // should be interpreted. See constructor implementation for reasoning behind these choices
    public static final boolean PATH = true;
    public static final boolean WALL = false;

    public static final int MAX_SIZE = 14;
    public static final int MIN_SIZE = 7;

    private boolean[][] maze;

    // REQUIRES: size must be in range [MIN_SIZE, MAX_SIZE]
    // EFFECTS: set the size of this maze and initialize the maze so that the top/bottom rows and left-most/right-most
    // columns are WALL
    public Maze(int size) {
        maze = new boolean[size][size];
        // boolean arrays are initialized to false, so the second part of the effects clause is automatically satisfied
        // given that WALL == false
    }

    // EFFECTS: produce the side-length of the maze
    @Override
    public int getSize() {
        return maze.length;
    }

    // REQUIRES: i and j must be in the range [0, size - 1]
    // EFFECTS: produce the value of the cell with given indices in the maze
    @Override
    public boolean getCell(int i, int j) {
        return maze[i][j];
    }

    // REQUIRES: i and j must be in the range [1, size - 2] (so the surrounding walls cannot be edited) and positions
    // (1, 1) and (size - 2, size - 2) can only be set to PATH
    // MODIFIES: this
    // EFFECTS: set the value of the cell with given indices in the maze
    public void setCell(int i, int j, boolean value) {
        maze[i][j] = value;
    }

}