package persistence;

import model.maze.Maze;
import model.moveable.Player;
import model.path.Position;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    private static final String DATA = "./data/readTest.json";

    @Test
    public void testReadNullFile() {
        try {
            new JsonReader("./data/data/test.json");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReadGeneric() throws IOException {
        JsonReader jsonReader = new JsonReader(DATA);
        Maze maze = jsonReader.readMaze();
        Maze mazeCompare = new Maze(7, "0000000010111001010100101010010001001111100000000");
        // verify loading properly; we test the constructor itself in MazeTest
        assertEquals(7, maze.getSize());
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                assertEquals(mazeCompare.isPath(x, y), maze.isPath(x, y));
            }
        }
        Player player = jsonReader.readPlayer(maze);
        assertEquals(new Position(1, 5), player.getPosition());
    }

}
