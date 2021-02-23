package persistence;

import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.moveable.Move;
import model.moveable.Player;
import model.path.Path;
import model.path.PathNode;
import model.path.Position;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

// handles reading of objects from a JSON file
// CITATION: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {

    private JSONObject obj;

    // EFFECTS: reads json from specified file; throws IOException if an error
    //          occurs reading data from file
    public JsonReader(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        obj = new JSONObject(contentBuilder.toString());
    }

    // EFFECTS: parses maze from JSON obj and returns it; throws NumberFormatException if
    //          JSON values are not integers
    public Maze readMaze() {
        JSONObject mazeObj = obj.getJSONObject("maze");
        return new Maze(mazeObj.getInt("size"), mazeObj.getString("maze"));
    }

    // EFFECTS: parses player from JSON obj and returns it
    public Player readPlayer(ImmutableMaze maze) {
        JSONObject playerObj = obj.getJSONObject("player");
        return new Player(maze, readPosField(playerObj), readPathField(playerObj));
    }

    // REQUIRES: playerObj must have a JSONObject under the key 'pos'
    // EFFECTS: parses Position object under key 'pos' from given JSONObject and returns it
    private static Position readPosField(JSONObject playerObj) {
        return readPos(playerObj.getJSONObject("pos"));
    }

    // REQUIRES: posObj must be JSON encoding of Position, produced by Position.toJSON
    // EFFECTS: parses given JSONObject as a Position object
    private static Position readPos(JSONObject posObj) {
        return new Position(posObj.getInt("x"), posObj.getInt("y"));
    }

    // REQUIRES: playerObj must have a JSONObject under the key 'path'
    // EFFECTS: parses Path object from given JSONObject under key 'path' and returns it
    private static Path readPathField(JSONObject playerObj) {
        JSONObject pathObj = playerObj.getJSONObject("path");
        List<PathNode> pathNodes = new ArrayList<>();
        Set<Position> visited = new HashSet<>();

        JSONObject node;
        Move move;
        for (Object obj : pathObj.getJSONArray("path")) {
            node = (JSONObject) obj;
            try {
                move = node.getEnum(Move.class, "direction");
            } catch (JSONException e) {
                move = null;
            }
            pathNodes.add(new PathNode(readPos(node), move));
        }

        for (Object obj : pathObj.getJSONArray("visited")) {
            visited.add(readPos((JSONObject) obj));
        }

        return new Path(pathNodes, visited);
    }

}
