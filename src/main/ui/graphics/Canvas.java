package ui.graphics;

import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.moveable.Move;
import model.moveable.Player;
import model.path.Path;
import model.path.PathEngine;
import model.path.PathNode;
import model.path.Position;
import model.solver.MazeSolver;
import ui.controller.MazeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

// A canvas that handles rendering of maze, player, and paths
public class Canvas extends JPanel {

    // size of a cell in pixels; must be odd and >= 7; 29, 13, 9 work very well. Smaller CELL_LENGTH == more cells!
    // in particular, choosing a number that can be written in the form '5 + 4n', for any n >= 0, will look smooth
    public static final int CELL_LENGTH = 17;
    public static final Color PATH_COLOR = Color.BLUE;
    public static final Color HEAD_COLOR = Color.DARK_GRAY;
    public static final int PATH_WIDTH = (CELL_LENGTH + 1) / 2;
    public static final Color PLAYER_COLOR = Color.RED;
    public static final int PLAYER_WIDTH = PATH_WIDTH + 2;
    public static final int FPS = 30;
    public static final String WIN_MESSAGE = "Congratulations, you have completed this maze! Press space or 'Generate"
            + " Maze' to try again.";

    private PathEngine pathEngine;

    // EFFECTS: initializes canvas to be specified size
    public Canvas(Dimension size) {
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setFocusable(true);

        pathEngine = new PathEngine();
    }

    // MODIFIES: g
    // EFFECTS: paints the current Maze, with squares of sidelength CELL_LENGTH, using black for WALL, and white for
    //          PATH & changes g's color to COLOR.WHITE
    public void paintMaze(Graphics g, ImmutableMaze maze) {
        wipeScreen(g);
        int size = maze.getSize();
        // fill everything with black so we get all the walls (internal & surrounding) done for free!
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, size * CELL_LENGTH, size * CELL_LENGTH);

        // draw insides of maze
        g.setColor(Color.WHITE);
        int cornerX = CELL_LENGTH;
        int cornerY = CELL_LENGTH;
        for (int y = 1; y < size - 1; y++) {
            for (int x = 1; x < size - 1; x++) {
                if (maze.isPath(x, y)) {
                    g.fillRect(cornerX, cornerY, CELL_LENGTH, CELL_LENGTH);
                }
                cornerX += CELL_LENGTH;
            }
            cornerX = CELL_LENGTH;
            cornerY += CELL_LENGTH;
        }
    }

    // MODIFIES: g, p
    // EFFECTS: applies move to Player and repaints, changes g's color to PLAYER_COLOR
    public void movePlayer(Graphics g, Move m, Player p) {
        g.setColor(Color.white);
        fill(g, p.getPosition());
        p.tryMove(m);
        paintPlayer(g, p);
    }

    // MODIFIES: g
    // EFFECTS: draws player at current position, changes g's color to PLAYER_COLOR
    public void paintPlayer(Graphics g, Player p) {
        g.setColor(PLAYER_COLOR);
        g.fillRect(CELL_LENGTH * (p.getPosition().getPosX()) + ((CELL_LENGTH - PLAYER_WIDTH) / 2),
                   CELL_LENGTH * (p.getPosition().getPosY()) + ((CELL_LENGTH - PLAYER_WIDTH) / 2),
                      PLAYER_WIDTH,
                      PLAYER_WIDTH);
    }

    // MODIFIES: this, g
    // EFFECTS: resets path handling & draws given path; changes color of g
    public void paintSingularPath(Graphics g, Path p) {
        wipePathCells(g, pathEngine.getPlaced());
        pathEngine = new PathEngine();
        g.setColor(PATH_COLOR);
        for (PathNode n : p.getNodes()) {
            paintPathNode(g, n);
        }
        // paint head/tail
        g.setColor(HEAD_COLOR);
        paintPathNode(g, p.getTail());
    }

    // MODIFIES: this, g, solver
    // EFFECTS: changes g's color, ticks solver to completion; if animate is true, animates the intermediate steps (at
    //          FPS steps per second), else simply draws the solution path
    public void paintSolver(Graphics g, ImmutableMaze maze, MazeSolver solver, boolean animate) {
        paintMaze(g, maze);
        pathEngine = new PathEngine();
        int delay = 1000 / FPS; // 1000 ms per second; this delay is in ms
        for (Path p : solver) {
            if (animate) {
                paintPathAnimate(g, p);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {

                }
            }
        }
        if (!animate) {
            paintSingularPath(g, solver.getPath());
        }
    }

    // MODIFIES: this, g
    // EFFECTS: paints all nodes in the given path & changes g's color
    private void paintPathAnimate(Graphics g, Path p) {
        pathEngine.addPath(p);
        wipePathCells(g, pathEngine.getToWipe());

        List<PathNode> toPaint = pathEngine.getToPlace();
        if (toPaint.size() > 0) {
            g.setColor(HEAD_COLOR);
            for (int i = 0; i < toPaint.size() - 1; i++) {
                paintPathNode(g, toPaint.get(i));
            }
            // paint head/tail
            g.setColor(PATH_COLOR);
            paintPathNode(g, toPaint.get(toPaint.size() - 1));
        }
    }

    // MODIFIES: g
    // EFFECTS: places white cells on all positions in given list & sets g's color to Color.WHITE
    public void wipePathCells(Graphics g, List<PathNode> cells) {
        g.setColor(Color.WHITE);
        for (PathNode cell : cells) {
            paintPathNode(g, cell);
        }
    }

    // EFFECTS: fills cell with specified Position with current color of g
    private void fill(Graphics g, Position pos) {
        g.fillRect(pos.getPosX() * CELL_LENGTH, pos.getPosY() * CELL_LENGTH, CELL_LENGTH, CELL_LENGTH);
    }

    // REQUIRES: g.setColor() must be set to appropriate color prior to calling this method (for efficiency)
    // EFFECTS: paints given PathNode
    private void paintPathNode(Graphics g, PathNode p) {
        Rectangle rect;
        if (p.getDirection() == null) {
            return;
        }
        switch (p.getDirection()) {
            case LEFT:
            case RIGHT:
                rect = getHorizontalPathBlock(p);
                break;
            case UP:
            case DOWN:
                rect = getVerticalPathBlock(p);
                break;
            default:
                return;
        }
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    // REQUIRES: direction is one-of UP or DOWN
    // EFFECTS: produces a rectangle representing PathNode with given pos and direction
    private Rectangle getVerticalPathBlock(PathNode p) {
        return new Rectangle(CELL_LENGTH * p.getPosX() + (CELL_LENGTH - PATH_WIDTH) / 2,
                p.getDirection() == Move.UP ? CELL_LENGTH * p.getPosY() + CELL_LENGTH / 2 - (PATH_WIDTH - 1) / 2
                                            : CELL_LENGTH * p.getPosY() - CELL_LENGTH / 2,
                        PATH_WIDTH,
                        CELL_LENGTH + (PATH_WIDTH - 1) / 2);
    }

    // REQUIRES: direction is one-of RIGHT or LEFT
    // EFFECTS: produces a rectangle representing PathNode with given pos and direction
    private Rectangle getHorizontalPathBlock(PathNode p) {
        return new Rectangle(
                CELL_LENGTH * p.getPosX() + (p.getDirection() == Move.LEFT ? CELL_LENGTH / 2 - (PATH_WIDTH - 1) / 2
                                                                              : -1 * CELL_LENGTH / 2),
                CELL_LENGTH * p.getPosY() + (CELL_LENGTH - PATH_WIDTH) / 2,
                CELL_LENGTH + (PATH_WIDTH - 1) / 2,
                PATH_WIDTH);
    }

    // MODIFIES: g
    // EFFECTS: paints a win message, changes g's color, tries to draw a congratulations image (prints error msg to
    //          console on failure)
    public void paintWin(Graphics g) {
        wipeScreen(g);
        g.setColor(Color.WHITE);

        g.setFont(MazeGame.HEADER);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(WIN_MESSAGE, g);
        int x = ((CELL_LENGTH * Maze.MAX_SIZE) - (int) r.getWidth()) / 2;
        int y = ((CELL_LENGTH * (Maze.MAX_SIZE - 15)) - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(WIN_MESSAGE, x, y);
    }

    // MODIFIES: g
    // EFFECTS: wipes the screen, changes g's color to MazeGame.BACKGROUND
    private void wipeScreen(Graphics g) {
        g.setColor(MazeGame.BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

}
