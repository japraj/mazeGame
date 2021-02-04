package ui.output;

import com.sun.javafx.scene.traversal.Direction;
import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.moveable.Move;
import model.moveable.Player;
import model.path.Path;
import model.path.PathNode;
import model.path.Position;

// Prints help messages and maze
public class Printer {

    private ImmutableMaze maze;

    // EFFECTS: set maze that will be printed
    public Printer(ImmutableMaze maze) {
        this.maze = maze;
    }

    // EFFECTS: print welcome message
    public void printWelcome() {
        System.out.println("Welcome to the Maze Simulator! Your character is represented by an 'X' character. Your"
                + " goal is to get to the bottom right corner of the maze.");
    }

    // EFFECTS: print help message
    public void printHelp() {
        System.out.print("Commands:\n"
                + "- gen n: generates a new maze of size n, n must be an odd integer between "
                + Maze.MIN_SIZE + " and " + Maze.MAX_SIZE + "\n"
                + "- solve: solves the maze from start\n"
                + "- animate-solve: shows an animation of the current maze being solved\n"
                + "- UP, RIGHT, LEFT, DOWN: these four commands allow you to move within the maze\n"
                + "- quit: exit the application\n\n");
    }

    // EFFECTS: print a message about invalid input
    public void printInvalidInput() {
        System.out.println("Invalid input; please try again");
    }

    // EFFECTS: print an error message
    public void printError() {
        System.out.println("Oops, something went wrong!");
    }

    // EFFECTS: prints congratulations message
    public void printCongratulations() {
        System.out.println("\nCongratulations, you win! Your player has been reset. you can continue playing the same"
                + " maze or try something else\n");
    }

    // REQUIRES: path must be within bounds of maze that this was initialized with
    // MODIFIES: path
    // EFFECTS: print the maze and path, using '*' for walls, ' ' for empty path, '<' for LEFT, '>' for RIGHT, 'v' for
    //          DOWN, '^' for UP, and 'x' for tail, and ticks path
    public void printPath(Path path) {
        path.reset();
        for (int y = 0; y < maze.getSize(); y++) {
            for (int x = 0; x < maze.getSize(); x++) {
                if (maze.getCell(x, y) == Maze.PATH) {
                    printPathNode(path, x, y);
                } else {
                    System.out.print("*");
                }
            }
            System.out.print('\n');
        }
    }

    // MODIFIES: path
    // EFFECTS: prints pathNode and ticks path
    private void printPathNode(Path path, int x, int y) {
        if (path.containsNode(x, y)) {
            PathNode node = path.getNode(x, y);
            if (path.getTail().equals(node)) {
                System.out.print("x");
            } else if (x == 1 && y == 1) {
                path.next();
                printMove(path.getDirection());
            } else {
                printMove(node.getDirection());
            }
        } else {
            System.out.print(" ");
        }
    }

    // EFFECTS: prints '<' for LEFT, '>' for RIGHT, 'v' for DOWN, and '^' for UP
    private void printMove(Move direction) {
        switch (direction) {
            case UP:
                System.out.print('^');
                break;
            case DOWN:
                System.out.print('v');
                break;
            case LEFT:
                System.out.print('<');
                break;
            case RIGHT:
                System.out.print('>');
                break;
        }
    }

    // REQUIRES: player position must be within bounds of maze that this was initialized with
    // EFFECTS: print the maze and player, using '*' for walls, ' ' for empty path, and 'x'
    // for the player
    public void printPlayerMaze(Player player) {
        Position playerPos = player.getPosition();
        for (int y = 0; y < maze.getSize(); y++) {
            for (int x = 0; x < maze.getSize(); x++) {
                if (playerPos.equals(x, y)) {
                    System.out.print('x');
                } else {
                    System.out.print(maze.getCell(x, y) == Maze.PATH ? " " : "*");
                }
            }
            System.out.print('\n');
        }
    }

    // EFFECTS: prints the given maze using '*' for WALL and ' ' for PATH
    public void printMaze() {
        printMaze(maze);
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
