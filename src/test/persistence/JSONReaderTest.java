package persistence;

import model.maze.Maze;
import model.moveable.Move;
import model.moveable.Player;
import model.path.Path;
import model.path.Position;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JSONReaderTest {

    private static final String DATA = "./data/readTest.json";

    @Test
    public void testReadNullFile() {
        try {
            new JSONReader("./data/data/test.json");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReadGeneric() throws IOException {
        JSONReader jsonReader = new JSONReader(DATA);
        Maze maze = jsonReader.readMaze();
        Maze mazeCompare = new Maze(7, "0000000010111001010100101010010001001111100000000");
        // verify loading properly; we test the constructor itself in MazeTest
        assertEquals(7, maze.getSize());
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                assertEquals(mazeCompare.getCell(x, y), maze.getCell(x, y));
            }
        }
        Player player = jsonReader.readPlayer(maze);
        Path comparePath = new Path();
        for (int i = 0; i < 4; i++) {
            // this assertion tests that visited was loaded properly
            assertTrue(player.getPath().visitedNode(comparePath.getTail()));
            comparePath.addNode(Move.DOWN);
        }
        assertTrue(player.getPath().visitedNode(comparePath.getTail()));
        assertEquals(comparePath, player.getPath());
        assertEquals(new Position(1, 5), player.getPosition());
    }

}
