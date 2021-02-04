package model;

import model.generator.MazeGenerator;
import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.moveable.Move;
import model.moveable.Player;
import model.path.Path;
import model.path.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;
    private final ImmutableMaze maze = new MazeGenerator(Maze.MIN_SIZE).generateMaze();

    @BeforeEach
    public void setup() {
        player = new Player(maze);
    }

    @Test
    public void testInit() {
        assertEquals(1, player.getPath().getLength());
        assertTrue(player.getPath().containsNode(new Position(1, 1)));
    }

    @Test
    public void testTryMove() {
        // try moving into the walls that are above and to the left of the init position (player should not move)
        player.tryMove(Move.UP);
        player.tryMove(Move.LEFT);
        assertEquals(1, player.getPath().getLength());
        assertTrue(player.getPosition().equals(1, 1));

        // find a cell in the maze that can be moved into (either right or down)
        Move next = maze.getCell(1, 2) ? Move.DOWN : Move.RIGHT;
        Path soln = new Path();
        soln.addNode(next);

        // check that the player can move in specified direction
        player.tryMove(next);
        assertEquals(soln, player.getPath());
        assertFalse(player.getPosition().equals(1, 1));
    }

}
