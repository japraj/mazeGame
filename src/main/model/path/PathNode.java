package model.path;

import com.sun.istack.internal.Nullable;
import model.moveable.Move;

// A Position in a Path that keeps track of how this position was obtained from the previous node in the Path
// that this is associated with; primary usage: rendering
public class PathNode extends Position {

    // direction that was moved in from the previous position in the path - null denotes the head of the path
    private Move direction;

    // REQUIRES: x, y must be non-negative and less than size, where size is the side-length of the maze that this
    //           PathNode is associated with, and Move must be the direction that was used to get to here from
    //           the previous node in the path (null val for path head)
    // EFFECTS: create a position with x, y and direction
    public PathNode(int posX, int posY, @Nullable Move direction) {
        super(posX, posY);
        this.direction = direction;
    }

    // EFFECTS: produce the direction that was used to get to this node from the previous node; null value denotes
    //          head of path
    public Move getDirection() {
        return direction;
    }

}
