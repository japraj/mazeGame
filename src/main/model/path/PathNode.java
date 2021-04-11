package model.path;

import model.moveable.Move;
import org.json.JSONObject;

// A Position in a Path that keeps track of how this position was obtained from the previous node in the Path
// that this is associated with; primary usage: rendering
public class PathNode extends Position {

    // direction that was moved in from the previous position in the path - null denotes the head of the path
    private Move direction;

    // REQUIRES: x, y must be non-negative and less than size, where size is the side-length of the maze that this
    //           PathNode is associated with, and Move must be the direction that was used to get to here from
    //           the previous node in the path (null val for path head)
    // EFFECTS: create a position with x, y and direction
    public PathNode(int posX, int posY, Move direction) {
        super(posX, posY);
        this.direction = direction;
    }

    // REQUIRES: posX and posY must be non-negative and less than size, where size is the side-length of the maze that
    //           this PathNode is associated with, and Move must be the direction that was used to get to here from
    //           the previous node in the path (null val for path head)
    // EFFECTS: create a node with specified direction and coordinates
    public PathNode(Position pos, Move direction) {
        super(pos.getPosX(), pos.getPosY());
        this.direction = direction;
    }

    // EFFECTS: produce the direction that was used to get to this node from the previous node; null value denotes
    //          head of path
    public Move getDirection() {
        return direction;
    }

    // EFFECTS: increment/decrement posX or posY depending on move (produces a new PathNode - does not modify this)
    @Override
    public PathNode applyMove(Move direction) {
        return new PathNode(super.applyMove(direction), direction);
    }

    // unused; only remains here in case we decide to store PathNodes, because it will use its inherited toJson method
    // and break any code (direction would not be saved)
    // EFFECTS: produces a JSON representation of this
    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        obj.put("direction", direction);
        return obj;
    }
}
