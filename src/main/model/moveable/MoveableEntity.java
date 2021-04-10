package model.moveable;

import model.maze.ImmutableMaze;
import model.path.Position;
import org.json.JSONObject;
import persistence.Saveable;

// An entity that can move within a maze
public abstract class MoveableEntity implements Saveable {

    protected ImmutableMaze maze;
    protected Position position;

    // REQUIRES: the value of the cell at initialPos in maze must be PATH
    // EFFECTS: sets the Maze that this entity moves within, and the initial position of the entity within the maze
    public MoveableEntity(ImmutableMaze maze, Position initialPos) {
        this.maze = maze;
        position = initialPos;
    }

    // EFFECTS: produce true if applying given move does not run this entity into a wall
    protected boolean isValid(Move move) {
        return maze.isPath(position.applyMove(move));
    }

    // MODIFIES: this
    // EFFECTS: if moving in specified manner does not run into a wall, apply the move
    public abstract void tryMove(Move move);

    // EFFECTS: produce current position of this entity
    public Position getPosition() {
        return position;
    }

    // EFFECTS: produces a JSON representation of this
    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("pos", position.toJson());
        return obj;
    }
}
