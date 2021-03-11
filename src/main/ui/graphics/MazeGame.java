package ui.graphics;

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

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

// Main MazeGame Window, integrates I/O, Generator/Solver/Maze
// CITATION: https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase.git
public class MazeGame extends JFrame {

    // Hard-coded Color Values & Fonts
    // Gray1 is the Darkest Gray and Gray4 is the Lightest Gray
    // Gray2 / Gray3 are in between
    private static final Color GRAY1 = new Color(37, 37, 39);
    private static final Color GRAY2 = new Color(45, 44, 49);
    private static final Color GRAY3 = new Color(62, 62, 70);
    private static final Color GRAY4 = new Color(68, 67, 73);
    private static final Color BLUE = new Color(0, 122, 206);
    private static final Color DULLBLUE = new Color(121, 170, 210);
    private static final Color LIGHTBLUE = new Color(181, 255, 255);

    // The following Colors are the ones being used in the GUI; never use the ones above!
    public static final Color BACKGROUND = GRAY1;
    public static final Color ACCENTS = BLUE;
    public static final Color TEXT_COLOR = Color.white;

    public static final Font HEADER = new Font("Dialog", Font.PLAIN, 15);
    public static final Font TEXT_FONT = new Font("Tahoma", Font.PLAIN, 13);

    // .json file path & tick Interval
    private static final String DATA = "./data/state.json";
    private static final int INTERVAL = 33;

    // models
    private MazeGenerator mazeGenerator;
    private int size;
    private ImmutableMaze maze;
    private MazeSolver solver;
    private Player player;
    private boolean blocked;
    // graphics
    private ConfigPanel config;
    private Canvas canvas;

    // Initializes app
    // EFFECTS: initialize simulation, configure window in which app is run and draws Maze on canvas
    public MazeGame() throws InterruptedException {
        // config
        super("MazeGame");
        getContentPane().setBackground(BACKGROUND);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setFocusable(true);
        addWindowListener(new WindowCloseHandler());

        // set size and center window
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((Maze.MAX_SIZE + 2) * Canvas.CELL_LENGTH + ConfigPanel.WIDTH, screen.height);
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);

        // init local vars
        init();

        // initialize & add children JPanels
        setLayout(new FlowLayout(FlowLayout.LEFT));
        config = new ConfigPanel(this, screen.height, size);
        canvas = new Canvas(screen);
        add(config);
        add(canvas);

        addKeyListeners();
        setVisible(true);

        // set up timer
        new Timer(INTERVAL, ae -> tick()).start();

        // draw Maze and player; call sleep to ensure everything is rendered already so the maze is drawn on top
        Thread.sleep(250);
        drawCanvas();
    }

    // MODIFIES: this
    // EFFECTS: ticks the simulation
    private void tick() {
        canvas.requestFocus();
    }

    // MODIFIES: this
    // EFFECTS: initialize simulator; try to load from data file and if that fails, use default values
    private void init() {
        blocked = false;
        try {
            load(false);
        } catch (Exception e) {
            size = Maze.MIN_SIZE;
            mazeGenerator = new MazeGenerator(size);
            maze = mazeGenerator.generateMaze();
            player = new Player(maze);
            solver = new FirstPath(maze, new Path());
        }
    }

    // MODIFIES: this
    // EFFECTS: loads fields from input file; throws Exception if file is not found or reader is unable to read from
    //          file
    public void load(boolean reset) throws Exception {
        JsonReader jsonReader = new JsonReader(DATA);

        maze = jsonReader.readMaze();
        size = maze.getSize();
        mazeGenerator = new MazeGenerator(size);
        player = jsonReader.readPlayer(maze);
        solver = new FirstPath(maze, new Path());

        if (reset) {
            drawCanvas();
        }
    }

    // EFFECTS: attempts to save data to the .json file with path DATA; if there is an issue with loading, prints a
    //          message to console
    public void save() {
        try {
            JsonWriter jsonWriter = new JsonWriter(DATA);
            jsonWriter.write(maze, player);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to load file");
        }
    }

    // EFFECTS: produces a graphics context that is translated to have its origin at
    //          (ConfigPanel.WIDTH + Canvas.CELL_LENGTH, getInsets().top)
    public Graphics getTranslatedGraphics() {
        Graphics g = getGraphics();
        g.translate(ConfigPanel.WIDTH + Canvas.CELL_LENGTH, getInsets().top);
        return g;
    }

    // MODIFIES: g
    // EFFECTS: paints maze and player, and changes color of g
    public void drawCanvas() {
        Graphics g = getTranslatedGraphics();
        canvas.paintMaze(g, maze);
        canvas.paintPlayer(g, player);
    }

    // MODIFIES: this
    // EFFECTS: solves maze and resets solver; animates solver as specified
    public void solve(boolean animate) {
        blocked = true;
        canvas.paintSolver(getTranslatedGraphics(), maze, solver, animate);
        solver.reset();
    }

    // MODIFIES: this
    // EFFECTS: resets player, repaints canvas
    public void reset() {
        player = new Player(maze);
        blocked = false;
        drawCanvas();
    }

    // MODIFIES: this
    // EFFECTS: if blocked is false, tries to move the player and repaints; if the player completes the maze, produces
    //          a congratulatory message. If blocked is true, does nothing
    private void move(Move move) {
        if (!blocked) {
            canvas.movePlayer(getTranslatedGraphics(), move, player);
            if (player.getPosition().equals(size - 2, size - 2)) {
                canvas.paintWin(getTranslatedGraphics());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if size is an odd int in the range [Maze.MIN_SIZE, Maze.MAX_SIZE], generates new maze of specified size
    //          else if size does not conform to those constraints, forces it to do so and then generates a maze of size
    public void generateNewMaze(int size) {
        // set size
        if (size < Maze.MIN_SIZE) {
            size = Maze.MIN_SIZE;
        } else if (size > Maze.MAX_SIZE) {
            size = Maze.MAX_SIZE;
        }
        size = size % 2 == 1 ? size : size + 1;
        // generate maze and update refs
        maze = mazeGenerator.generateMaze(size);
        solver = new FirstPath(maze, new Path());
        reset();
    }

    // MODIFIES: this
    // EFFECTS: adds a KeyListener to this, canvas, and config
    private void addKeyListeners() {
        KeyHandler keyHandler = new KeyHandler();
        addKeyListener(keyHandler);
        canvas.addKeyListener(keyHandler);
        config.addKeyListener(keyHandler);
    }

    private class KeyHandler extends KeyAdapter {
        // MODIFIES: this MazeGame instance
        // EFFECTS: if input is an arrow key, tries to move Player in specified direction, if input is space, resets
        //          player to maze start, else does nothing
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_KP_UP:
                    move(Move.UP);
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_KP_DOWN:
                    move(Move.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_KP_LEFT:
                    move(Move.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_KP_RIGHT:
                    move(Move.RIGHT);
                    break;
                case KeyEvent.VK_SPACE:
                    reset();
                    break;
            }
        }
    }

    private class WindowCloseHandler extends WindowAdapter {
        // EFFECTS: attempts to save data to the .json file with path DATA on windowClosing event and terminates
        //          application; if there is an issue,
        @Override
        public void windowClosing(WindowEvent event) {
            save();
            System.exit(0);
        }
    }
}
