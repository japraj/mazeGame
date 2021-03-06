package model;

import model.generator.MazeGenerator;
import model.maze.ImmutableMaze;
import model.maze.Maze;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {

    private Maze maze;
    private static final int SIZE = Maze.MIN_SIZE;

    @BeforeEach
    public void setup() {
        maze = new Maze(SIZE);
    }

    @Test
    public void testConstructor() {
        // test that the constructor initialized the maze to the correct size and that its surroundings are to WALL
        assertEquals(SIZE, maze.getSize());

        // test left/rightt col and top/bottom row
        for (int i = 0; i < SIZE; i++) {
            assertFalse(maze.isPath(i, 0));
            assertFalse(maze.isPath(0, i));
            assertFalse(maze.isPath(i, SIZE - 1));
            assertFalse(maze.isPath(SIZE - 1, i));
        }
    }

    @Test
    public void testAltConstructor() {
        MazeGenerator generator = new MazeGenerator(11);
        ImmutableMaze generatedMaze = generator.generateMaze();
        Maze constructedMaze = new Maze(11, generatedMaze.toString());
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                assertEquals(generatedMaze.isPath(x, y), constructedMaze.isPath(x, y));
            }
        }
    }

    @Test
    public void testSetCell() {
        // set 1,1 true
        maze.setCell(1, 1, true);
        assertTrue(maze.isPath(1, 1));
        // set 1,1 false
        maze.setCell(1, 1, false);
        assertFalse(maze.isPath(1, 1));
        // set 1,2 true and 2,1 false
        maze.setCell(1, 2, true);
        maze.setCell(2, 1, false);
        assertTrue(maze.isPath(1, 2));
        assertFalse(maze.isPath(2, 1));
    }

    @Test
    public void testFloorOdd() {
        assertEquals(3, Maze.floorOdd(3.2));
        assertEquals(3, Maze.floorOdd(4.2));
        assertEquals(3, Maze.floorOdd(3));
        assertEquals(5, Maze.floorOdd(6.232));
    }

}
