package persistence;

import model.maze.ImmutableMaze;
import model.moveable.Player;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// handles writing to a JSON file
// CITATION: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;

    // EFFECTS: opens writer to write to destination file; throws FileNotFoundException if destination file cannot be
    //          opened for writing
    public JsonWriter(String destination) throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of maze and player to file
    public void write(ImmutableMaze maze, Player player) {
        JSONObject obj = new JSONObject();
        obj.put("maze", maze.toJson());
        obj.put("player", player.toJson());
        saveToFile(obj.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
