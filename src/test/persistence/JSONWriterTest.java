package persistence;

import model.maze.Maze;
import model.moveable.Move;
import model.moveable.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JSONWriterTest {

    private static final String DATA = "./data/writeTest.json";

    @Test
    public void testNullWrite() {
        try {
            new JSONWriter("./data/data/test.json");
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testGenericWrite() throws IOException {
        Maze maze = new Maze(7, "0000000010111001010100101010010001001111100000000");
        Player player = new Player(maze);
        for (int i = 0; i < 4; i++) {
            player.tryMove(Move.DOWN);
        }

        JSONWriter jsonWriter = new JSONWriter(DATA);
        jsonWriter.write(maze, player);
        jsonWriter.close();

        JSONReader jsonReader = new JSONReader(DATA);

        Maze readMaze = jsonReader.readMaze();
        assertEquals(7, readMaze.getSize());
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                assertEquals(maze.getCell(x, y), readMaze.getCell(x, y));
            }
        }

        Player readPlayer = jsonReader.readPlayer(readMaze);
        assertEquals(player.getPath(), readPlayer.getPath());
        assertEquals(player.getPosition(), readPlayer.getPosition());
    }
}
