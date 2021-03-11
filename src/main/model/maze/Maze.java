package model.maze;

import model.path.Position;
import org.json.JSONObject;
import ui.graphics.Canvas;

import java.awt.*;

// A square Maze; each cell is a boolean (true and false represent PATH and WALL, respectively). A maze is started at
// the top left, at position (1, 1) and terminate in the bottom right, at position (size - 2, size - 2); the maze
// does in fact use zero-based indexing, but all Mazes are surrounded by wall (i.e. the top and bottom rows, and
// left-most and right-most columns are all WALL). All mazes must be solveable starting at the top left. This means
// that all mazes have PATH at positions (1, 1) and (size - 2, size - 2), and there is some way to move between them.
public class Maze implements ImmutableMaze {

    // these static variables should be used in the stead of magic constants - they represent how a cell of the Maze
    // should be interpreted. See constructor implementation for reasoning behind these choices
    public static final boolean PATH = true;
    public static final boolean WALL = false;

    public static final int MAX_SIZE = 31;
//    public static final int MAX_SIZE = floorOdd(
//                                        Toolkit.getDefaultToolkit().getScreenSize().getHeight()
//                                            / Canvas.CELL_LENGTH);
    public static final int MIN_SIZE = 7;

    // this class provides an abstraction on top of this 2-dimensional array - all methods use x, y notation instead of
    // the i, j notation associated with matrices; y determines row while x determines column, so x, y means maze[y][x]
    private boolean[][] maze;

    // REQUIRES: size must be odd and in the range [MIN_SIZE, MAX_SIZE]
    // EFFECTS: set the size of this maze and initialize the maze so that the top/bottom rows and left-most/right-most
    // columns are WALL
    public Maze(int size) {
        size = floorOdd(size);
        maze = new boolean[size][size];
        // boolean arrays are initialized to false, so the second part of the effects clause is automatically satisfied
        // given that WALL == false
    }

    // REQUIRES: size must be an odd integer in the range [MIN_SIZE, MAX_SIZE] and encoding must have been produced by
    //           Maze.toString
    // MODIFIES: this
    // EFFECTS: sets the size of this and initializes maze with respect to encoding
    public Maze(int size, String encoding) {
        maze = new boolean[size][size]; // see above constructor for explanation
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                maze[y][x] = encoding.charAt(x + y * size) == '0' ? WALL : PATH;
            }
        }
    }

    // EFFECTS: produce the side-length of the maze
    @Override
    public int getSize() {
        return maze.length;
    }

    // REQUIRES: x and y must be in the range [0, size - 1]
    // EFFECTS: produce the value of the cell with given indices in the maze
    @Override
    public boolean getCell(int x, int y) {
        return maze[y][x];
    }

    // REQUIRES: posX and posY must be in the range [0, size - 1]
    // EFFECTS: produce the value of the cell with given position in the maze
    @Override
    public boolean getCell(Position pos) {
        return maze[pos.getPosY()][pos.getPosX()];
    }

    // REQUIRES: i and j must be in the range [1, size - 2] (so the surrounding walls cannot be edited) and positions
    // (1, 1) and (size - 2, size - 2) can only be set to PATH
    // MODIFIES: this
    // EFFECTS: set the value of the cell with given indices in the maze
    public void setCell(int x, int y, boolean value) {
        maze[y][x] = value;
    }


    // REQUIRES: poxX and posY must be in the range [1, size - 2] (so the surrounding walls cannot be edited) and
    // positions (1, 1) and (size - 2, size - 2) can only be set to PATH
    // MODIFIES: this
    // EFFECTS: set the cell with given position to specified value
    public void setCell(Position pos, boolean value) {
        maze[pos.getPosY()][pos.getPosX()] = value;
    }

    // EFFECTS: produces a String representation of the maze represented by this; notation: convert 2d array into
    //          a binary int, with 0 == WALL and 1 == PATH
    @Override
    public String toString() {
        StringBuilder bint = new StringBuilder(); // bint == binary integer
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze.length; x++) {
                bint.append(maze[y][x] == WALL ? 0 : 1);
            }
        }
        return bint.toString();
    }

    // EFFECTS: produces a JSON representation of this
    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("size", maze.length);
        obj.put("maze", toString());
        return obj;
    }

    // EFFECTS: produces the largest odd integer less than arg
    private static int floorOdd(double arg) {
        return (int) arg % 2 == 0 ? (int) arg - 1 : (int) arg;
    }
}
