package model;

import model.generator.MazeGenerator;
import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.moveable.Move;
import model.moveable.Player;
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
        assertEquals(new Position(1, 1), player.getPosition());
    }

    @Test
    public void testTryMove() {
        // try moving into the walls that are above and to the left of the init position (player should not move)
        player.tryMove(Move.UP);
        player.tryMove(Move.LEFT);
        assertTrue(player.getPosition().equals(1, 1));

        // find a cell in the maze that can be moved into (either right or down)
        Move next = maze.isPath(1, 2) ? Move.DOWN : Move.RIGHT;

        // check that the player can move in specified direction
        player.tryMove(next);
        assertEquals(new Position(1, 1).applyMove(next), player.getPosition());
        assertFalse(player.getPosition().equals(1, 1));
    }

}
