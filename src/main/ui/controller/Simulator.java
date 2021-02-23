package ui.controller;

import model.generator.MazeGenerator;
import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.moveable.Move;
import model.moveable.Player;
import model.path.Path;
import model.solver.FirstPath;
import model.solver.MazeSolver;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.output.Printer;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// Integrate I/O, Tokenizer, and Generator/Solver/Maze
public class Simulator {

    private static final List<String> MOVES = Arrays.asList("up", "down", "left", "right");
    private static final String DATA = "./data/state.json";

    // models
    private MazeGenerator mazeGenerator;
    private int size;
    private ImmutableMaze maze;
    private MazeSolver solver;
    private Player player;
    // input/output
    private Scanner reader;
    private Printer printer;
    private String input;

    // EFFECTS: initialize and start the app
    public Simulator() {
        init();

        try {
            run();
        } catch (InterruptedException e) {
            printer.printError();
        } catch (FileNotFoundException e) {
            printer.printSaveError();
        }
    }

    // MODIFIES: this
    // EFFECTS: initialize this; try to read from .json file and if that fails, revert to default values
    private void init() {
        // model
        try {
            JsonReader jsonReader = new JsonReader(DATA);

            maze = jsonReader.readMaze();
            size = maze.getSize();
            mazeGenerator = new MazeGenerator(size);
            player = jsonReader.readPlayer(maze);
        } catch (Exception e) {
            e.printStackTrace();
            // if reading json file fails, use default values
            size = Maze.MIN_SIZE;
            mazeGenerator = new MazeGenerator(size);
            maze = mazeGenerator.generateMaze();
            player = new Player(maze);
        }
        solver = new FirstPath(maze, new Path());

        // i/o
        reader = new Scanner(System.in);
        printer = new Printer(maze);
    }

    // EFFECTS: save maze and player to json file
    private void save() throws FileNotFoundException {
        JsonWriter jsonWriter = new JsonWriter(DATA);
        jsonWriter.write(maze, player);
        jsonWriter.close();
    }

    // MODIFIES: this
    // EFFECTS: runs the app
    private void run() throws InterruptedException, FileNotFoundException {
        printer.printWelcome();
        printer.printHelp();
        printer.printPlayerMaze(player);
        while (true) {
            if (player.getPath().getTail().equals(size - 2, size - 2)) {
                win();
            }
            input = reader.nextLine().toLowerCase().trim().replaceAll("\\s+", " ");
            if (MOVES.contains(input)) {
                movePlayer();
            } else if (input.startsWith("gen")) {
                generateMaze();
            } else if (input.equals("solve")) {
                solve();
            } else if (input.equals("animate-solve")) {
                animateSolve();
            } else if (input.equals("quit")) {
                save();
                return;
            } else {
                printer.printInvalidInput();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: prints congratulations, help message, and maze, and resets player position
    private void win() {
        player = new Player(maze);

        printer.printCongratulations();
        printer.printHelp();
        printer.printPlayerMaze(player);
    }

    // MODIFIES: this
    // EFFECTS: moves player according to input and prints maze
    private void movePlayer() {
        switch (input) {
            case "up":
                player.tryMove(Move.UP);
                break;
            case "down":
                player.tryMove(Move.DOWN);
                break;
            case "left":
                player.tryMove(Move.LEFT);
                break;
            case "right":
                player.tryMove(Move.RIGHT);
                break;
        }
        printer.printPlayerMaze(player);
    }

    // MODIFIES: this
    // EFFECTS: animates solving of maze and resets player/solver
    private void animateSolve() throws InterruptedException {
        while (!solver.isSolved()) {
            printer.printPath(solver.tick());
            Thread.sleep(500);
        }
        solver.reset();
        player = new Player(maze);
    }

    // MODIFIES: this
    // EFFECTS: prints solution to maze and resets player/solver
    private void solve() {
        while (!solver.isSolved()) {
            solver.tick();
        }
        printer.printPath(solver.getPath());
        solver.reset();
        player = new Player(maze);
    }

    // MODIFIES: this
    // EFFECTS: if input is of form 'gen n' with n being an integer:
    //              - if n is an odd int in the range [Maze.MIN_SIZE, Maze.MAX_SIZE], generates new maze of size n
    //              - if n does not conform to those constraints, forces it to do so and then generates a maze of size n
    //          else prints invalid input message
    private void generateMaze() {
        try {
            // set size
            size = Integer.parseInt(input.split("\\s")[1]);
            if (size < Maze.MIN_SIZE) {
                size = Maze.MIN_SIZE;
            } else if (size > Maze.MAX_SIZE) {
                size = Maze.MAX_SIZE;
            }
            size = size % 2 == 1 ? size : size + 1;
            // generate maze and update refs
            maze = mazeGenerator.generateMaze(size);
            solver = new FirstPath(maze, new Path());
            player = new Player(maze);
            printer = new Printer(maze);
            printer.printPlayerMaze(player);
        } catch (Exception e) {
            printer.printInvalidInput();
        }
    }

}
