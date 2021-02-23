package model.moveable;

import model.maze.ImmutableMaze;
import model.path.Path;
import model.path.Position;
import org.json.JSONObject;
import persistence.Saveable;

// An entity that can move within a maze and keeps track of its path
public class Player extends MoveableEntity implements Saveable {

    private Path path;

    // EFFECTS: initialize the player at position (1,1)
    public Player(ImmutableMaze maze) {
        super(maze, new Position(1, 1));
        path = new Path();
    }

    // EFFECTS: initialize the player with given path and position
    public Player(ImmutableMaze maze, Position pos, Path path) {
        super(maze, pos);
        this.path = path;
    }

    // MODIFIES: this
    // EFFECTS: if moving in specified manner does not run into a wall, apply the move AND add it to current path
    @Override
    public void tryMove(Move move) {
        if (isValid(move)) {
            position = position.applyMove(move);
            path.addNode(move);
        }
    }

    // EFFECTS: produce path player has taken so far
    public Path getPath() {
        return path;
    }

    // EFFECTS: produces a JSON representation of this
    @Override
    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put("path", path.toJSON());
        return obj;
    }
}
