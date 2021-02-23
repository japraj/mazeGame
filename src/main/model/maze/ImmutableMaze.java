package model.maze;

import model.path.Position;
import persistence.Saveable;

// A square, read-only maze; see class-level comment of Maze for a detailed description
public interface ImmutableMaze extends Saveable {

    // EFFECTS: produce the side-length of the maze
    int getSize();

    // REQUIRES: x and y must be in the range [0, size - 1]
    // EFFECTS: produce the value of the cell with given indices in the maze
    boolean getCell(int x, int y);

    // REQUIRES: posX and posY must be in the range [0, size - 1]
    // EFFECTS: produce the value of the cell with given position in the maze
    boolean getCell(Position pos);
}
