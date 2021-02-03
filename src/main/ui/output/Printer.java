package ui.output;

import model.maze.ImmutableMaze;
import model.maze.Maze;
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

    // EFFECTS: prints the given maze using '*' for WALL and ' ' for PATH
    public static void printMaze(ImmutableMaze maze) {
        for (int y = 0; y < maze.getSize(); y++) {
            for (int x = 0; x < maze.getSize(); x++) {
                System.out.print(maze.getCell(x, y) == Maze.PATH ? " " : "*");
            }
            System.out.print('\n');
        }
    }

}
