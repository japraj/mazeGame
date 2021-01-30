package ui.output;

import model.maze.ImmutableMaze;
import model.path.Path;
import model.path.Position;

// Prints help messages and maze
public class Printer {

    private ImmutableMaze maze;

    // EFFECTS: set maze that will be printed
    public Printer(ImmutableMaze maze) {

    }

    // EFFECTS: print welcome message
    public void printWelcome() {

    }

    // EFFECTS: print help message
    public void printHelp() {

    }

    // REQUIRES: path must be within bounds of maze that this was initialized with
    // EFFECTS: print the maze and path, using '*' for walls, ' ' for empty path,
    // '<' for LEFT, '>' for RIGHT, 'v' for DOWN, '^' for UP
    public void printPath(Path solution) {

    }

    // REQUIRES: player position must be within bounds of maze that this was initialized with
    // EFFECTS: print the maze and player, using '*' for walls, ' ' for empty path, and 'x'
    // for the player
    public void printPlayerMaze(Position player) {

    }

}
