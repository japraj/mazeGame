package model;

import model.maze.ImmutableMaze;
import model.maze.Maze;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {

    private Maze maze;
    // SIZE must be >= 4
    private static final int SIZE = 4;

    @BeforeEach
    public void setup() {
        maze = new Maze(SIZE);
    }

    @Test
    public void testConstructor() {
        // test that the constructor initialized the maze to the correct size and that its surroundings are to WALL
        assertEquals(SIZE, maze.getSize());

        // test left col and top row
        for (int i = 0; i < SIZE; i++) {
            assertEquals(Maze.WALL, maze.getCell(i, 0));
            assertEquals(Maze.WALL, maze.getCell(0, i));
        }
        // test right col and bottom row
        for (int i = 0; i < SIZE; i++) {
            assertEquals(Maze.WALL, maze.getCell(i, SIZE - 1));
            assertEquals(Maze.WALL, maze.getCell(SIZE - 1, i));
        }
    }

    @Test
    public void testSetCell() {
        // set 1,1 true
        maze.setCell(1, 1, true);
        assertTrue(maze.getCell(1, 1));
        // set 1,1 false
        maze.setCell(1, 1, false);
        assertFalse(maze.getCell(1, 1));
        // set 1,2 true and 2,1 false
        maze.setCell(1, 2, true);
        maze.setCell(2, 1, false);
        assertTrue(maze.getCell(1, 2));
        assertFalse(maze.getCell(2, 1));
    }

}
