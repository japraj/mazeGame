package model.path;

import model.moveable.Move;
import org.json.JSONObject;
import persistence.Saveable;

// A position with x, y in a maze; positions are immutable.
public class Position implements Saveable {

    private int posX;
    private int posY;

    // REQUIRES: x, y must be non-negative and less than size, where size is the side-length of the maze that this
    //           position is associated with
    // EFFECTS: initialize this position as (x, y)
    public Position(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    // EFFECTS: produce x coordinate of this position
    public int getPosX() {
        return posX;
    }

    // EFFECTS: produce y coordinate of this position
    public int getPosY() {
        return posY;
    }

    // EFFECTS: increment/decrement posX or posY depending on move (produces a new position - does not modify this)
    public Position applyMove(Move move) {
        switch (move) {
            case UP:
                return new Position(posX, posY - 1);
            case DOWN:
                return new Position(posX, posY + 1);
            case LEFT:
                return new Position(posX - 1, posY);
            case RIGHT:
            default:
                return new Position(posX + 1, posY);
        }
    }

    // EFFECTS: produce true if given object is Position and o and this have same posX and posY
    @Override
    public boolean equals(Object o) {
        return o instanceof Position && this.posX == ((Position) o).getPosX() && this.posY == ((Position) o).getPosY();
    }

    // EFFECTS: produce true if this has same coordinates as given
    public boolean equals(int x, int y) {
        return posX == x && posY == y;
    }

    // EFFECTS: produce a unique hashcode for this
    @Override
    public int hashCode() {
        return Integer.parseInt(posX + "000" + posY);
    }

    // EFFECTS: produces a JSON representation of this
    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("x", posX);
        obj.put("y", posY);
        return obj;
    }
}
