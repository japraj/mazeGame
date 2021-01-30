package model.maze;

// A square, read-only maze; see class-level comment of Maze for a detailed description
public interface ImmutableMaze {

    // EFFECTS: produce the side-length of the maze
    int getSize();

    // REQUIRES: i and j must be in the range [0, size - 1]
    // EFFECTS: produce the value of the cell with given indices in the maze
    boolean getCell(int i, int j);

}
